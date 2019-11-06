/*
 * Copyright 2015 The CHOReVOLUTION project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.syncope.core.logic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import eu.chorevolution.chors.ChorSpecUtils;
import eu.chorevolution.datamodel.Choreography;
import eu.chorevolution.datamodel.ExistingService;
import eu.chorevolution.idm.common.ChorevolutionEntitlement;
import eu.chorevolution.idm.common.to.ChoreographyTO;
import eu.chorevolution.idm.common.types.ChoreographyAction;
import eu.chorevolution.idm.common.types.ChoreographyOperation;
import eu.chorevolution.idm.common.types.SecurityFilterInfo;
import eu.chorevolution.idm.common.types.SecurityFilterStatus;
import eu.chorevolution.idm.common.types.ServiceAction;
import eu.chorevolution.securityfilter.api.SecurityFilterConfiguration;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import org.apache.commons.collections4.IterableUtils;
import org.apache.cxf.helpers.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.common.util.Base64Utility;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.syncope.common.lib.AbstractBaseBean;
import org.apache.syncope.common.lib.SyncopeConstants;
import org.apache.syncope.common.lib.patch.AnyObjectPatch;
import org.apache.syncope.common.lib.patch.AttrPatch;
import org.apache.syncope.common.lib.patch.GroupPatch;
import org.apache.syncope.common.lib.patch.MembershipPatch;
import org.apache.syncope.common.lib.patch.StringReplacePatchItem;
import org.apache.syncope.common.lib.to.AnyObjectTO;
import org.apache.syncope.common.lib.to.AnyTypeClassTO;
import org.apache.syncope.common.lib.to.AttrTO;
import org.apache.syncope.common.lib.to.GroupTO;
import org.apache.syncope.common.lib.to.MembershipTO;
import org.apache.syncope.common.lib.to.PlainSchemaTO;
import org.apache.syncope.common.lib.to.RelationshipTO;
import org.apache.syncope.common.lib.to.TypeExtensionTO;
import org.apache.syncope.common.lib.types.AnyTypeKind;
import org.apache.syncope.common.lib.types.AttrSchemaType;
import org.apache.syncope.common.lib.types.CipherAlgorithm;
import org.apache.syncope.common.lib.types.PatchOperation;
import org.apache.syncope.common.lib.types.SchemaType;
import org.apache.syncope.core.persistence.api.dao.ChoreographyDAO;
import org.apache.syncope.core.persistence.api.dao.ChoreographyInstanceDAO;
import org.apache.syncope.core.persistence.api.dao.NotFoundException;
import org.apache.syncope.core.persistence.api.dao.search.AnyTypeCond;
import org.apache.syncope.core.persistence.api.dao.search.AttributeCond;
import org.apache.syncope.core.persistence.api.dao.search.MembershipCond;
import org.apache.syncope.core.persistence.api.dao.search.OrderByClause;
import org.apache.syncope.core.persistence.api.dao.search.SearchCond;
import org.apache.syncope.core.persistence.api.entity.group.GPlainAttr;
import org.apache.syncope.core.persistence.api.entity.group.Group;
import org.apache.syncope.core.spring.security.Encryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
public class ChoreographyLogic extends AbstractLogic<AbstractBaseBean> {

    private static final int PAGE_SIZE = 100;

    private static final String SERVICE_TYPE = "SERVICE";

    private static final String SERVICE_ROLE_TYPE = "SERVICE ROLE";

    private static final String ENACTMENT_ENGINE_TYPE = "ENACTMENT ENGINE";

    private static final String ENACTMENT_ENGINE_BASE_URL = "enactmentEngineBaseURL";

    private static final String ENACTMENT_ENGINE_USERNAME = "enactmentEngineUsername";

    private static final String ENACTMENT_ENGINE_PASSWORD = "enactmentEnginePassword";

    private static final String SYNTHESIS_PROCESSOR_TYPE = "SYNTHESIS PROCESSOR";

    private static final String CHOREOGRAPHY_ID_SCHEMA = "id";

    private static final String CHOREOGRAPHY_STATUS_SCHEMA = "status";

    private static final String SERVICE_LOCATION_SCHEMA = "Service Location";

    private static final String GLOBAL_SECURITY_FILTER_ENDPOINT_SCHEMA = "GlobalSecurityFilterEndpoint";

    private static final String SECURITY_FILTER_ENDPOINT_SCHEMA = "SecurityFilterEndpoint";

    private static final String CHOREOGRAPHY_URL_PATH = "/choreography";

    private static String SECRET_KEY;

    static {
        InputStream propStream = null;
        try {
            propStream = Encryptor.class.getResourceAsStream("/security.properties");
            Properties props = new Properties();
            props.load(propStream);

            SECRET_KEY = props.getProperty("secretKey");
        } catch (IOException e) {
            LOG.error("Could not read security parameters", e);
        } finally {
            org.apache.commons.io.IOUtils.closeQuietly(propStream);
        }

    }

    @Autowired
    private GroupLogic groupLogic;

    @Autowired
    private AnyObjectLogic anyObjectLogic;

    @Autowired
    private AnyTypeClassLogic anyTypeClassLogic;

    @Autowired
    private SchemaLogic schemaLogic;

    @Autowired
    private ChoreographyDAO choreographyDAO;

    @Autowired
    private ChoreographyInstanceDAO choreographyInstanceDAO;

    private WebClient getEEWebClient(final String enactmentEngineKey, final String endpoint) throws Exception {
        AnyObjectTO enactmentEngine = anyObjectLogic.read(enactmentEngineKey);
        if (!ENACTMENT_ENGINE_TYPE.equals(enactmentEngine.getType())) {
            throw new NotFoundException("Enactment Engine instance with key " + enactmentEngineKey);
        }

        return getEEWebClient(enactmentEngine, endpoint);
    }

    private WebClient getEEWebClient(final AnyObjectTO enactmentEngine, final String endpoint) throws Exception {
        Map<String, AttrTO> plainAttrs = enactmentEngine.getPlainAttrMap();

        String baseURL = null;
        if (plainAttrs.containsKey(ENACTMENT_ENGINE_BASE_URL)) {
            List<String> values = plainAttrs.get(ENACTMENT_ENGINE_BASE_URL).getValues();
            if (!values.isEmpty()) {
                baseURL = values.get(0);
            }
        }
        if (baseURL == null) {
            throw new IllegalArgumentException(
                    "Could not find " + ENACTMENT_ENGINE_BASE_URL + " for " + enactmentEngine.getKey());
        }

        String username = null;
        if (plainAttrs.containsKey(ENACTMENT_ENGINE_USERNAME)) {
            List<String> values = plainAttrs.get(ENACTMENT_ENGINE_USERNAME).getValues();
            if (!values.isEmpty()) {
                username = values.get(0);
            }
        }
        String password = null;
        if (plainAttrs.containsKey(ENACTMENT_ENGINE_PASSWORD)) {
            List<String> values = plainAttrs.get(ENACTMENT_ENGINE_PASSWORD).getValues();
            if (!values.isEmpty()) {
                PlainSchemaTO enactmentEnginePasswordSchema =
                        schemaLogic.read(SchemaType.PLAIN, ENACTMENT_ENGINE_PASSWORD);
                password = Encryptor.getInstance(enactmentEnginePasswordSchema.getSecretKey()).
                        decode(values.get(0), enactmentEnginePasswordSchema.getCipherAlgorithm());
            }
        }

        List<Object> providers = new ArrayList<>();
        providers.add(new JacksonJaxbJsonProvider());
        return WebClient.create(
                StringUtils.removeEndIgnoreCase(baseURL, "/") + CHOREOGRAPHY_URL_PATH + endpoint,
                providers, username, password, null).
                accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_XML_TYPE);
    }

    private WebClient getSFWebClient(final GroupTO choreography, final AnyObjectTO service, final String endpoint) {
        MembershipTO membership = service.getMembershipMap().get(choreography.getKey());
        if (membership == null) {
            throw new NotFoundException(
                    "Service " + service.getName() + " not involved in choreography " + choreography.getName());
        }

        AttrTO securityFilterEndpoint = membership.getPlainAttrMap().get(SECURITY_FILTER_ENDPOINT_SCHEMA);
        if (securityFilterEndpoint == null || securityFilterEndpoint.getValues().isEmpty()) {
            throw new NotFoundException(
                    "No security filter for " + service.getName() + " in choreography " + choreography.getName());
        }

        List<Object> providers = new ArrayList<>();
        providers.add(new JacksonJaxbJsonProvider());
        return WebClient.create(
                StringUtils.removeEndIgnoreCase(securityFilterEndpoint.getValues().get(0), "/")
                + "/management/" + endpoint, providers).
                accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_JSON_TYPE);
    }

    private AnyObjectTO serviceExists(final String id) {
        AnyObjectTO serviceTO = anyObjectLogic.read(id);
        if (!SERVICE_TYPE.equals(serviceTO.getType())) {
            throw new NotFoundException(SERVICE_TYPE + " " + id);
        }
        return serviceTO;
    }

    private AnyObjectTO enactmentEngineForChoreography(final String choreographyKey) {
        MembershipCond membershipCond = new MembershipCond();
        membershipCond.setGroup(choreographyKey);
        AnyTypeCond anyTypeCond = new AnyTypeCond();
        anyTypeCond.setAnyTypeKey(ENACTMENT_ENGINE_TYPE);
        SearchCond serviceCond = SearchCond.getAndCond(
                SearchCond.getLeafCond(anyTypeCond), SearchCond.getLeafCond(membershipCond));

        List<AnyObjectTO> candidates =
                anyObjectLogic.search(serviceCond, 1, 1, Collections.emptyList(), SyncopeConstants.ROOT_REALM, false);
        if (candidates.isEmpty()) {
            throw new NotFoundException("Enactment Engine for choreography " + choreographyKey);
        }

        return candidates.get(0);
    }

    private AnyObjectTO synthesisProcessorForChoreography(final String choreographyKey) {
        MembershipCond membershipCond = new MembershipCond();
        membershipCond.setGroup(choreographyKey);
        AnyTypeCond anyTypeCond = new AnyTypeCond();
        anyTypeCond.setAnyTypeKey(SYNTHESIS_PROCESSOR_TYPE);
        SearchCond serviceCond = SearchCond.getAndCond(
                SearchCond.getLeafCond(anyTypeCond), SearchCond.getLeafCond(membershipCond));

        List<AnyObjectTO> candidates =
                anyObjectLogic.search(serviceCond, 1, 1, Collections.emptyList(), SyncopeConstants.ROOT_REALM, false);
        if (candidates.isEmpty()) {
            throw new NotFoundException("Synthesis Processor for choreography " + choreographyKey);
        }

        return candidates.get(0);
    }

    private void updateChoreographyStatus(final GroupTO choreography, final String newStatus) {
        GroupPatch choreographyPatch = new GroupPatch();
        choreographyPatch.setKey(choreography.getKey());
        choreographyPatch.getTypeExtensions().addAll(choreography.getTypeExtensions());
        choreographyPatch.getPlainAttrs().add(new AttrPatch.Builder().attrTO(
                new AttrTO.Builder().schema(CHOREOGRAPHY_STATUS_SCHEMA).value(newStatus).build()
        ).build());

        groupLogic.update(choreographyPatch, false);
    }

    private AnyObjectTO enactmentEngineExists(final String enactmentEngineKey) {
        // ensures that enactmentEngine contains effectively the entity key and that
        // such object exists and has the expected type
        AnyObjectTO enactmentEngine = anyObjectLogic.read(enactmentEngineKey);
        if (!ENACTMENT_ENGINE_TYPE.equals(enactmentEngine.getType())) {
            LOG.error("Could not find Enactment Engine with key {}",
                    enactmentEngine.getKey());
            throw new BadRequestException("Could not find Enactment Engine with key "
                    + enactmentEngine.getKey());
        }

        return enactmentEngine;
    }

    private AnyObjectTO synthesisProcessorExists(final String synthesisProcessorKey) {
        // ensures that synthesisProcessor contains effectively the entity key and that
        // such object exists and has the expected type
        AnyObjectTO synthesisProcessor = anyObjectLogic.read(synthesisProcessorKey);
        if (!SYNTHESIS_PROCESSOR_TYPE.equals(synthesisProcessor.getType())) {
            LOG.error("Could not find Synthesis Processor with key {}",
                    synthesisProcessor.getKey());
            throw new BadRequestException("Could not find Synthesis Processor with key "
                    + synthesisProcessor.getKey());
        }

        return synthesisProcessor;
    }

    @PreAuthorize("hasRole('" + ChorevolutionEntitlement.CHOREOGRAPHY_READ + "')")
    public ChoreographyTO getChoreography(final String choreographyKey) {
        GroupTO choreography = groupLogic.read(choreographyKey);
        if (choreography == null) {
            return null;
        }
        return getChoreography(choreography, true);
    }

    @PreAuthorize("hasRole('" + ChorevolutionEntitlement.CHOREOGRAPHY_LIST + "')")
    public List<ChoreographyTO> getChoreographyList() {
        List<ChoreographyTO> result = new LinkedList<>();

        for (Group choreography : choreographyDAO.findAll()) {
            ChoreographyTO choreographyTO = new ChoreographyTO();
            choreographyTO.setKey(choreography.getKey());
            choreographyTO.setName(choreography.getName());

            GPlainAttr description = choreography.getPlainAttr("description");
            if (description != null && !description.getValues().isEmpty()) {
                choreographyTO.setDescription(description.getValues().get(0).getStringValue());
            }
            result.add(choreographyTO);
        }
        return result;
    }

    @PreAuthorize("hasRole('" + ChorevolutionEntitlement.CHOREOGRAPHY_CREATE + "')")
    public String create(final ChoreographyTO choreographyTO) {
        AnyObjectTO synthesisProcessor = synthesisProcessorExists(choreographyTO.getSynthesisProcessorKey());

        GroupTO choreography = new GroupTO();
        try {
            // 1. create the choreography group
            choreography.setName(choreographyTO.getName());
            choreography.setRealm(SyncopeConstants.ROOT_REALM);
            choreography.getAuxClasses().add("Choreography");
            choreography.getPlainAttrs().add(
                    new AttrTO.Builder().schema(CHOREOGRAPHY_STATUS_SCHEMA).value("CREATED").build());
            choreography.getPlainAttrs().add(
                    new AttrTO.Builder().schema("chorSpec").
                            value(Base64Utility.encode(choreographyTO.getChorspec())).build());
            choreography.getPlainAttrs().add(
                    new AttrTO.Builder().schema("diagram").
                            value(Base64Utility.encode(choreographyTO.getDiagram())).build());
            choreography.getPlainAttrs().add(
                    new AttrTO.Builder().schema("messages").
                            value(Base64Utility.encode(choreographyTO.getMessages())).build());

            if (choreographyTO.getImage() != null && choreographyTO.getImage().length > 0) {
                choreography.getPlainAttrs().add(
                    new AttrTO.Builder().schema("image").
                            value(Base64Utility.encode(choreographyTO.getImage())).build());
            }

            if (choreographyTO.getDescription() != null) {
                choreography.getPlainAttrs().add(
                    new AttrTO.Builder().schema("description").value(choreographyTO.getDescription()).build());
            }

            TypeExtensionTO serviceTE = new TypeExtensionTO();
            serviceTE.setAnyType(SERVICE_TYPE);
            serviceTE.getAuxClasses().add("Deployed");
            choreography.getTypeExtensions().add(serviceTE);

            choreography = groupLogic.create(choreography, false).getEntity();

            // 2. set the membership with the given synthesis processor
            AnyObjectPatch eePatch = new AnyObjectPatch();
            eePatch.setKey(synthesisProcessor.getKey());
            eePatch.getMemberships().add(new MembershipPatch.Builder().group(choreography.getKey()).build());
            anyObjectLogic.update(eePatch, false);

        } catch (Exception e) {
            throw new RuntimeException("While creating " + choreographyTO.getName(), e);
        }

        return choreography.getKey();
    }

    @PreAuthorize("hasRole('" + ChorevolutionEntitlement.CHOREOGRAPHY_UPDATE + "')")
    public void update(final ChoreographyTO choreographyTO) {
        GroupTO choreography = groupLogic.read(choreographyTO.getKey());

        GroupPatch choreographyPatch = new GroupPatch();
        choreographyPatch.setKey(choreographyTO.getKey());
        choreographyPatch.getTypeExtensions().addAll(choreography.getTypeExtensions());
        try {
            // Updates only choreography description and image
            if (choreographyTO.getDescription() != null) {
                choreographyPatch.getPlainAttrs().add(new AttrPatch.Builder().attrTO(
                    new AttrTO.Builder().schema("description").value(choreographyTO.getDescription()).build()).build());
            }

            if (choreographyTO.getImage() != null && choreographyTO.getImage().length > 0) {
                choreographyPatch.getPlainAttrs().add(new AttrPatch.Builder().attrTO(
                    new AttrTO.Builder().schema("image").
                            value(Base64Utility.encode(choreographyTO.getImage())).build()).build());
            }
            groupLogic.update(choreographyPatch, false);
        } catch (Exception e) {
            throw new RuntimeException("While updating " + choreographyTO.getName(), e);
        }
    }

    @PreAuthorize("hasRole('" + ChorevolutionEntitlement.CHOREOGRAPHY_DELETE + "')")
    public void delete(final String key) {
        GroupTO choreography = groupLogic.read(key);

        Map<String, AttrTO> choreographyAttrs = choreography.getPlainAttrMap();
        if (!choreographyAttrs.containsKey(CHOREOGRAPHY_STATUS_SCHEMA)
                || choreographyAttrs.get(CHOREOGRAPHY_STATUS_SCHEMA).getValues().isEmpty()
                || !"CREATED".equals(choreographyAttrs.get(CHOREOGRAPHY_STATUS_SCHEMA).getValues().get(0))) {

            AnyObjectTO enactmentEngine = enactmentEngineForChoreography(choreography.getKey());
            try {
                WebClient webClient = getEEWebClient(enactmentEngine,
                        "/" + choreography.getPlainAttrMap().get("id").getValues().get(0));
                Response response = webClient.delete();
                if (response.getStatus() != Response.Status.ACCEPTED.getStatusCode()) {
                    throw new WebApplicationException(response);
                }
            } catch (Exception e) {
                throw new RuntimeException("While deleting " + choreography.getName(), e);
            }

            // finally update the choreograpy status
            updateChoreographyStatus(choreography, "PENDING DELETE");

        } else {
            groupLogic.delete(choreography.getKey(), false);
        }

    }

    private GroupTO choreographyExists(final SearchCond cond) {
        AttributeCond isChoreographyCond = new AttributeCond(AttributeCond.Type.EQ);
        isChoreographyCond.setSchema("isChoreography");
        isChoreographyCond.setExpression("true");

        List<GroupTO> candidates = groupLogic.search(SearchCond.getAndCond(
                cond, SearchCond.getLeafCond(isChoreographyCond)),
                1, 1, Collections.<OrderByClause>emptyList(), SyncopeConstants.ROOT_REALM, false);
        if (candidates.isEmpty()) {
            throw new NotFoundException("Choreography matching " + cond);
        }

        return candidates.get(0);
    }

    private GroupTO choreographyExists(final String id) {
        AttributeCond idCond = new AttributeCond(AttributeCond.Type.EQ);
        idCond.setSchema(CHOREOGRAPHY_ID_SCHEMA);
        idCond.setExpression(id);

        return choreographyExists(SearchCond.getLeafCond(idCond));
    }

    @PreAuthorize("hasRole('" + ChorevolutionEntitlement.CHOREOGRAPHY_ENACT + "')")
    public void enact(final String key, final String enactmentEngineKey) {
        GroupTO choreography = groupLogic.read(key);

        // Enactment Engine selection
        AnyObjectTO enactmentEngine;
        try {
            // Prefer always the enactment engine specified as argument
            enactmentEngine = enactmentEngineForChoreography(enactmentEngineKey);
        } catch (NotFoundException e) {
            try {
                // If the EE was not already selected, search for the enactment engine and set the relationship
                enactmentEngine = enactmentEngineExists(enactmentEngineKey);

                // Remove all previous memberships of EE with the selected choreography
                MembershipCond membershipCond = new MembershipCond();
                membershipCond.setGroup(choreography.getKey());
                AnyTypeCond anyTypeCond = new AnyTypeCond();
                anyTypeCond.setAnyTypeKey(ENACTMENT_ENGINE_TYPE);
                SearchCond serviceCond = SearchCond.getAndCond(
                        SearchCond.getLeafCond(anyTypeCond), SearchCond.getLeafCond(membershipCond));

                for (AnyObjectTO service : anyObjectLogic.search(serviceCond,
                        1, -1, Collections.emptyList(), SyncopeConstants.ROOT_REALM, false)) {

                    AnyObjectPatch patch = new AnyObjectPatch();
                    patch.setKey(service.getKey());
                    patch.getMemberships().add(new MembershipPatch.Builder().
                            operation(PatchOperation.DELETE).group(choreography.getKey()).build());
                    anyObjectLogic.update(patch, false);
                }

                // Add membership with the selected EE
                AnyObjectPatch eePatch = new AnyObjectPatch();
                eePatch.setKey(enactmentEngine.getKey());
                eePatch.getMemberships().add(new MembershipPatch.Builder().group(choreography.getKey()).build());
                anyObjectLogic.update(eePatch, false);
            } catch (Exception ex) {
                throw new RuntimeException("While enacting choreography " + choreography.getKey(), ex);
            }
        }

        Map<String, AttrTO> choreographyAttrs = choreography.getPlainAttrMap();
        try {
            InputStream chorSpec = new ByteArrayInputStream(
                    Base64Utility.decode(choreographyAttrs.get("chorSpec").getValues().get(0)));

            if (choreographyAttrs.containsKey(CHOREOGRAPHY_STATUS_SCHEMA)
                    && !choreographyAttrs.get(CHOREOGRAPHY_STATUS_SCHEMA).getValues().isEmpty()
                    && "CREATED".equals(choreographyAttrs.get(CHOREOGRAPHY_STATUS_SCHEMA).getValues().get(0))) {

                String generatedChoreographyId = null;

                Response response;
                WebClient webClient =
                        getEEWebClient(enactmentEngine.getKey(), "/?choreographyName=" + choreography.getName());
                response = webClient.post(chorSpec);
                if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
                    throw new WebApplicationException(response);
                }

                if (response.hasEntity()) {
                    String responseBody = IOUtils.toString((InputStream) response.getEntity());
                    LOG.debug("Response from Enactment Engine {}:\n{}", enactmentEngine.getKey(), responseBody);

                    JsonNode node = new ObjectMapper().readTree(responseBody);
                    if (node.has("entityId")) {
                        generatedChoreographyId = node.get("entityId").asText();
                    }
                }
                if (StringUtils.isEmpty(generatedChoreographyId)) {
                    throw new RuntimeException("Could not extract the generated choreography id");
                }

                GroupPatch choreographyPatch = new GroupPatch();
                choreographyPatch.setKey(choreography.getKey());
                choreographyPatch.getTypeExtensions().addAll(choreography.getTypeExtensions());
                choreographyPatch.getPlainAttrs().add(new AttrPatch.Builder().attrTO(
                        new AttrTO.Builder().schema(CHOREOGRAPHY_ID_SCHEMA).value(generatedChoreographyId).build()).
                        build());
                choreographyPatch.getPlainAttrs().add(new AttrPatch.Builder().attrTO(
                        new AttrTO.Builder().schema(CHOREOGRAPHY_STATUS_SCHEMA).value("PENDING ENACTMENT").build()).
                        build());
                groupLogic.update(choreographyPatch, false);
            } else {
                String id = choreographyAttrs.get(CHOREOGRAPHY_ID_SCHEMA).getValues().get(0);
                WebClient webClient
                        = getEEWebClient(enactmentEngine, "/" + id + "?choreographyName=" + choreography.getName());
                Response response = webClient.put(chorSpec);
                if (response.getStatus() != Response.Status.ACCEPTED.getStatusCode()) {
                    throw new WebApplicationException(response);
                }

                updateChoreographyStatus(choreography, "PENDING ENACTMENT");
            }
        } catch (Exception e) {
            throw new RuntimeException("While enacting on " + enactmentEngine.getKey(), e);
        }
    }

    private String getUSERTypeExtensionKey(final String name) {
        return name + " USER Type Extension";
    }

    private void processChorSpec(
            final Choreography chorSpec, final String choreographykey, final AnyTypeClassTO classForUserTE) {

        chorSpec.getServiceGroups().forEach((serviceGroup) -> {
            serviceGroup.getServices().stream().
                    filter((service) -> (service instanceof ExistingService)).forEachOrdered((service) -> {
                try {
                    AnyObjectTO serviceTO = anyObjectLogic.read(service.getName());
                    if (!SERVICE_TYPE.equals(serviceTO.getType())) {
                        throw new NotFoundException(SERVICE_TYPE + " " + service.getName());
                    }

                    // add the relevant services to the group, according to the chorSpec,
                    // and save the security filter URL
                    AnyObjectPatch servicePatch = new AnyObjectPatch();
                    servicePatch.setKey(serviceTO.getKey());

                    MembershipPatch membershipPatch = new MembershipPatch.Builder().group(choreographykey).build();

                    String securityFilterURL = ChorSpecUtils.findSecurityFilterURL(chorSpec, service);
                    if (securityFilterURL != null) {
                        membershipPatch.getPlainAttrs().add(new AttrPatch.Builder().
                                attrTO(new AttrTO.Builder().
                                        schema(SECURITY_FILTER_ENDPOINT_SCHEMA).value(securityFilterURL).build()).
                                build());
                    }
                    servicePatch.getMemberships().add(membershipPatch);

                    anyObjectLogic.update(servicePatch, false);

                    // for each service requiring per-user authentication, create the
                    // Username and Password schemas (if not found), for USER type extension
                    AttrTO serviceAuth = serviceTO.getPlainAttrMap().get("Service Authentication Type");
                    if (serviceAuth != null && !serviceAuth.getValues().isEmpty()
                            && serviceAuth.getValues().get(0).equals("PER_USER")) {

                        PlainSchemaTO username = new PlainSchemaTO();
                        username.setKey(service.getName() + " Username");
                        try {
                            schemaLogic.read(SchemaType.PLAIN, username.getKey());
                        } catch (NotFoundException e) {
                            username.setType(AttrSchemaType.String);
                            username.setMandatoryCondition("true");
                            schemaLogic.create(SchemaType.PLAIN, username);
                        }
                        classForUserTE.getPlainSchemas().add(username.getKey());

                        PlainSchemaTO password = new PlainSchemaTO();
                        password.setKey(service.getName() + " Password");
                        try {
                            schemaLogic.read(SchemaType.PLAIN, password.getKey());
                        } catch (NotFoundException e) {
                            password.setType(AttrSchemaType.Encrypted);
                            password.setCipherAlgorithm(CipherAlgorithm.AES);
                            password.setSecretKey(SECRET_KEY);
                            password.setMandatoryCondition("true");
                            schemaLogic.create(SchemaType.PLAIN, password);
                        }
                        classForUserTE.getPlainSchemas().add(password.getKey());
                    }
                } catch (NotFoundException e) {
                    LOG.error("Could not find service {} in the inventory, ignoring", service.getName(), e);
                }
            });
        });
    }

    @PreAuthorize("hasRole('" + ChorevolutionEntitlement.NOTIFY_COMPLETION + "')")
    public void notifyCompletion(
            final String id,
            final String name,
            final ChoreographyOperation operation,
            final String message,
            final InputStream enactedChorSpec) {

        LOG.debug("Receiving notification about {} operation on choreography {} {}, with message '{}'",
                operation, id, name, message);

        byte[] chorSpecXML = null;
        Choreography chorSpec = null;
        List<String> globalSecurityFilterURLs = Collections.emptyList();
        if (operation != ChoreographyOperation.DELETE) {
            if (enactedChorSpec != null) {
                try {
                    chorSpecXML = IOUtils.readBytesFromStream(enactedChorSpec);
                    chorSpec = Choreography.fromXML(new ByteArrayInputStream(chorSpecXML));
                } catch (IOException | JAXBException | XMLStreamException e) {
                    LOG.error("While reading enacted chorSpec for choreography {}", id, e);
                    throw new BadRequestException("While reading enacted chorSpec for choreography " + id, e);
                }
            }
            if (chorSpec == null) {
                LOG.error("Cloud not parse enacted chorSpec for choreography {}", id);
                throw new BadRequestException("Cloud not parse enacted chorSpec for choreography " + id);
            }

            globalSecurityFilterURLs = ChorSpecUtils.findGlobalSecurityFilterURLs(chorSpec);
        }

        GroupTO choreography = choreographyExists(id);
        String oldName = choreography.getName();

        GroupPatch choreographyPatch = new GroupPatch();
        choreographyPatch.setKey(choreography.getKey());
        choreographyPatch.getTypeExtensions().addAll(choreography.getTypeExtensions());

        AnyTypeClassTO classForUserTE;
        switch (operation) {
            case CREATE:
                // 1. update the group
                if (!oldName.equals(name)) {
                    choreographyPatch.setName(new StringReplacePatchItem.Builder().value(name).build());
                }
                choreographyPatch.getPlainAttrs().add(new AttrPatch.Builder().attrTO(
                        new AttrTO.Builder().schema(CHOREOGRAPHY_STATUS_SCHEMA).value("STARTED").build()).build());
                if (!globalSecurityFilterURLs.isEmpty()) {
                    choreographyPatch.getPlainAttrs().add(new AttrPatch.Builder().attrTO(new AttrTO.Builder().
                            schema(GLOBAL_SECURITY_FILTER_ENDPOINT_SCHEMA).value(globalSecurityFilterURLs.get(0)).
                            build()).build());
                }
                choreographyPatch.getPlainAttrs().add(new AttrPatch.Builder().attrTO(
                        new AttrTO.Builder().schema("enactedChorSpec").value(Base64Utility.encode(chorSpecXML))
                                .build()).build());

                // 2. process the chorSpec
                classForUserTE = new AnyTypeClassTO();
                classForUserTE.setKey(getUSERTypeExtensionKey(name));

                processChorSpec(chorSpec, choreography.getKey(), classForUserTE);

                if (!classForUserTE.getPlainSchemas().isEmpty()) {
                    anyTypeClassLogic.create(classForUserTE);

                    TypeExtensionTO userTE = new TypeExtensionTO();
                    userTE.setAnyType(AnyTypeKind.USER.name());
                    userTE.getAuxClasses().add(classForUserTE.getKey());

                    choreographyPatch.getTypeExtensions().add(userTE);
                }

                groupLogic.update(choreographyPatch, false);
                break;

            case UPDATE:
                // 1. update the group
                if (!oldName.equals(name)) {
                    choreographyPatch.setName(new StringReplacePatchItem.Builder().value(name).build());
                }
                choreographyPatch.getPlainAttrs().add(new AttrPatch.Builder().attrTO(
                        new AttrTO.Builder().schema(CHOREOGRAPHY_STATUS_SCHEMA).value("STARTED").build()).build());
                if (globalSecurityFilterURLs.isEmpty()) {
                    choreographyPatch.getPlainAttrs().add(new AttrPatch.Builder().attrTO(new AttrTO.Builder().
                            schema(GLOBAL_SECURITY_FILTER_ENDPOINT_SCHEMA).build()).
                            operation(PatchOperation.DELETE).build());
                } else {
                    choreographyPatch.getPlainAttrs().add(new AttrPatch.Builder().attrTO(new AttrTO.Builder().
                            schema(GLOBAL_SECURITY_FILTER_ENDPOINT_SCHEMA).value(globalSecurityFilterURLs.get(0)).
                            build()).build());
                }
                choreographyPatch.getPlainAttrs().add(new AttrPatch.Builder().attrTO(
                        new AttrTO.Builder().schema("enactedChorSpec").value(Base64Utility.encode(chorSpecXML))
                                .build()).build());

                choreography = groupLogic.update(choreographyPatch, false).getEntity();

                // 2. remove all SERVICE members
                MembershipCond membershipCond = new MembershipCond();
                membershipCond.setGroup(choreography.getKey());
                AnyTypeCond anyTypeCond = new AnyTypeCond();
                anyTypeCond.setAnyTypeKey(SERVICE_TYPE);
                SearchCond serviceCond = SearchCond.getAndCond(
                        SearchCond.getLeafCond(anyTypeCond), SearchCond.getLeafCond(membershipCond));

                int count = anyObjectLogic.searchCount(serviceCond, SyncopeConstants.ROOT_REALM);
                for (int page = 1; page <= (count / PAGE_SIZE) + 1; page++) {
                    for (AnyObjectTO service : anyObjectLogic.search(serviceCond,
                            page, PAGE_SIZE, Collections.emptyList(), SyncopeConstants.ROOT_REALM, false)) {

                        AnyObjectPatch patch = new AnyObjectPatch();
                        patch.setKey(service.getKey());
                        patch.getMemberships().add(new MembershipPatch.Builder().
                                operation(PatchOperation.DELETE).group(choreography.getKey()).build());
                        anyObjectLogic.update(patch, false);
                    }
                }

                // 3. process the chorSpec
                classForUserTE = anyTypeClassLogic.read(oldName);

                processChorSpec(chorSpec, choreography.getKey(), classForUserTE);

                if (oldName.equals(name)) {
                    anyTypeClassLogic.update(classForUserTE);
                } else {
                    classForUserTE.setKey(getUSERTypeExtensionKey(name));
                    anyTypeClassLogic.delete(getUSERTypeExtensionKey(oldName));
                    anyTypeClassLogic.create(classForUserTE);

                    TypeExtensionTO userTE = choreography.getTypeExtension(AnyTypeKind.USER.name());
                    userTE.getAuxClasses().clear();
                    userTE.getAuxClasses().add(classForUserTE.getKey());

                    choreographyPatch = new GroupPatch();
                    choreographyPatch.setKey(choreography.getKey());
                    choreographyPatch.getTypeExtensions().addAll(choreography.getTypeExtensions());
                    groupLogic.update(choreographyPatch, false);
                }
                break;

            case DELETE:
                // 1. remove the schemas and the USER type extension
                try {
                    classForUserTE = anyTypeClassLogic.read(getUSERTypeExtensionKey(choreography.getName()));
                    classForUserTE.getPlainSchemas().forEach((plainSchema) -> {
                        schemaLogic.delete(SchemaType.PLAIN, plainSchema);
                    });

                    anyTypeClassLogic.delete(classForUserTE.getKey());
                } catch (NotFoundException e) {
                    LOG.debug("Unexpected: could not find AnyTypeClass {}",
                            getUSERTypeExtensionKey(choreography.getName()), e);
                }

                // 2. remove the choreography group and all related choreography instances and events
                groupLogic.delete(choreography.getKey(), false);

                choreographyInstanceDAO.deleteByChoreographyId(id);
                break;

            default:
        }
    }

    @PreAuthorize("hasRole('" + ChorevolutionEntitlement.ON_CHOREOGRAPHY + "')")
    public void onChoreography(
            final String key,
            final ChoreographyAction action,
            final Integer newSize) {

        GroupTO choreography = groupLogic.read(key);
        String choregraphyId = choreography.getPlainAttrMap().get("id").getValues().get(0);
        AnyObjectTO enactmentEngine = enactmentEngineForChoreography(key);

        String newStatus = null;
        try {
            WebClient webClient = null;
            switch (action) {
                case START:
                case UNFREEZE:
                    webClient = getEEWebClient(enactmentEngine, "/" + choregraphyId + "/start");
                    newStatus = "STARTED";
                    break;

                case STOP:
                    webClient = getEEWebClient(enactmentEngine, "/" + choregraphyId + "/stop");
                    newStatus = "STOPPED";
                    break;

                case FREEZE:
                    webClient = getEEWebClient(enactmentEngine, "/" + choregraphyId + "/pause");
                    newStatus = "FROZEN";
                    break;

                case RESIZE:
                    webClient = getEEWebClient(enactmentEngine, "/" + choregraphyId + "/resize?newSize=" + newSize);
                    break;

                default:
            }

            if (webClient != null) {
                Response response = webClient.post(null);
                if (response.getStatus() != Response.Status.ACCEPTED.getStatusCode()) {
                    throw new WebApplicationException(response);
                }
            }

            if (newStatus != null) {
                updateChoreographyStatus(choreography, newStatus);
            }
        } catch (Exception e) {
            throw new RuntimeException(
                    "While acting on " + choreography.getName(), e);
        }
    }

    @PreAuthorize("hasRole('" + ChorevolutionEntitlement.ON_CHOREOGRAPHY_SERVICE + "')")
    public void onChoreographyService(
            final String key,
            final String serviceId,
            final ServiceAction action,
            final String argument) {

        GroupTO choreography = groupLogic.read(key);
        AnyObjectTO enactmentEngine = enactmentEngineForChoreography(choreography.getKey());
        AnyObjectTO service = serviceExists(serviceId);

        MembershipTO membership = service.getMembershipMap().get(choreography.getKey());
        if (membership == null) {
            throw new NotFoundException(
                    "Service " + service.getName() + " not involved in choreography " + choreography.getName());
        }

        if (action == ServiceAction.REPLACE) {
            AnyObjectTO newService = serviceExists(argument);
            RelationshipTO relationship = IterableUtils.find(
                    newService.getRelationships(),
                    (RelationshipTO object) -> SERVICE_ROLE_TYPE.equals(object.getRightType()));
            if (relationship == null) {
                throw new NotFoundException(SERVICE_ROLE_TYPE + " for service " + newService.getName());
            }
            AnyObjectTO newServiceRole = anyObjectLogic.read(relationship.getRightKey());

            AttrTO serviceLocation = newService.getPlainAttrMap().get(SERVICE_LOCATION_SCHEMA);
            if (serviceLocation == null || serviceLocation.getValues().isEmpty()) {
                throw new NotFoundException(SERVICE_LOCATION_SCHEMA + " for service " + newService.getName());
            }

            try {
                WebClient webClient = getEEWebClient(enactmentEngine,
                        "/" + choreography.getPlainAttrMap().get("id").getValues().get(0)
                        + "/replaceService/" + newServiceRole.getName() + "/" + newService.getName()
                        + "?serviceEndPoint=" + serviceLocation);
                Response response = webClient.post(null);
                if (response.getStatus() != Response.Status.ACCEPTED.getStatusCode()) {
                    throw new WebApplicationException(response);
                }
            } catch (Exception e) {
                throw new RuntimeException("While replacing service", e);
            }
        } else {
            WebClient webClient;
            Response response = null;
            try {
                switch (action) {
                    case ENABLE_SECURITY_FILTER:
                        webClient = getSFWebClient(
                                choreography, service, SecurityFilterStatus.ENABLED.name());
                        response = webClient.put(null);
                        break;

                    case DISABLE_SECURITY_FILTER:
                        webClient = getSFWebClient(
                                choreography, service, SecurityFilterStatus.DISABLED.name());
                        response = webClient.put(null);
                        break;

                    case ENFORCE_SECURITY_CONTEXT:
                        webClient = getSFWebClient(
                                choreography, service, "securityContext");
                        response = webClient.post(argument);
                        break;

                    default:
                }

                if (response != null && response.getStatus() != Response.Status.OK.getStatusCode()) {
                    throw new WebApplicationException(response);
                }
            } catch (WebApplicationException e) {
                throw new RuntimeException(
                        "While requesting " + action + " on security filter for " + service.getName()
                        + " in choreography " + choreography.getName(), e);
            }
        }
    }

    @PreAuthorize("hasRole('" + ChorevolutionEntitlement.ON_CHOREOGRAPHY_SERVICE + "')")
    public SecurityFilterInfo readSecurityFilterInfo(final String key, final String serviceId) {
        GroupTO choreography = groupLogic.read(key);
        AnyObjectTO service = serviceExists(serviceId);

        try {
            WebClient webClient = getSFWebClient(choreography, service, "info");
            Response response = webClient.get();
            if (response.getStatus() != Response.Status.OK.getStatusCode()) {
                throw new WebApplicationException(response);
            }
            return response.readEntity(SecurityFilterInfo.class);
        } catch (WebApplicationException e) {
            throw new RuntimeException("While reading security filter information for " + service.getName()
                    + " in choreography " + choreography.getName(), e);
        }
    }

    @PreAuthorize("hasRole('" + ChorevolutionEntitlement.ON_CHOREOGRAPHY_SERVICE + "')")
    public void configureSecurityFilter(
            final String key,
            final String serviceId,
            final URL federationServerURL) {
        GroupTO choreography = groupLogic.read(key);
        AnyObjectTO service = serviceExists(serviceId);

        try {
            WebClient webClient = getSFWebClient(choreography, service, "federationServerURL");
            SecurityFilterConfiguration sfc = new SecurityFilterConfiguration();
            sfc.setURL(federationServerURL.toExternalForm());
            Response response = webClient.put(sfc);
            if (response.getStatus() != Response.Status.OK.getStatusCode()) {
                throw new WebApplicationException(response);
            }
        } catch (WebApplicationException e) {
            throw new RuntimeException("While setting the Federation Server UTL into the Security Filter instance for "
                    + service.getName() + " in choreography " + choreography.getName(), e);
        }
    }

    private ChoreographyTO getChoreography(final GroupTO choreography, final boolean details) {
        ChoreographyTO choreographyTO = new ChoreographyTO();
        choreographyTO.setKey(choreography.getKey());
        choreographyTO.setName(choreography.getName());

        AttrTO id = choreography.getPlainAttrMap().get("id");
        if (id != null && !id.getValues().isEmpty()) {
            choreographyTO.setChoreographyId(id.getValues().get(0));
        }

        AttrTO description = choreography.getPlainAttrMap().get("description");
        if (description != null && !description.getValues().isEmpty()) {
            choreographyTO.setDescription(description.getValues().get(0));
        }

        AttrTO status = choreography.getPlainAttrMap().get("status");
        if (status != null && !status.getValues().isEmpty()) {
            choreographyTO.setStatus(status.getValues().get(0));
        }

        if (details) {
            AttrTO chorspec = choreography.getPlainAttrMap().get("chorSpec");
            if (chorspec != null && !chorspec.getValues().isEmpty()) {
                choreographyTO.setChorspec(DatatypeConverter.parseBase64Binary(chorspec.getValues().get(0)));
            }

            AttrTO messages = choreography.getPlainAttrMap().get("messages");
            if (messages != null && !messages.getValues().isEmpty()) {
                choreographyTO.setMessages(DatatypeConverter.parseBase64Binary(messages.getValues().get(0)));
            }

            AttrTO diagram = choreography.getPlainAttrMap().get("diagram");
            if (diagram != null && !diagram.getValues().isEmpty()) {
                choreographyTO.setDiagram(DatatypeConverter.parseBase64Binary(diagram.getValues().get(0)));
            }

            AttrTO image = choreography.getPlainAttrMap().get("image");
            if (image != null && !image.getValues().isEmpty()) {
                choreographyTO.setImage(DatatypeConverter.parseBase64Binary(image.getValues().get(0)));
            }
        }

        return choreographyTO;
    }

    @PreAuthorize("hasRole('" + ChorevolutionEntitlement.CHOREOGRAPHY_LIST + "')")
    public AnyObjectTO getChoreographyEnactmentEngine(final String choreographyKey) {
        groupLogic.read(choreographyKey);
        return enactmentEngineForChoreography(choreographyKey);
    }

    @PreAuthorize("hasRole('" + ChorevolutionEntitlement.CHOREOGRAPHY_LIST + "')")
    public AnyObjectTO getChoreographySynthesisProcessor(final String choreographyKey) {
        groupLogic.read(choreographyKey);
        return synthesisProcessorForChoreography(choreographyKey);
    }

    @Override
    protected AbstractBaseBean resolveReference(final Method method, final Object... args)
            throws UnresolvedReferenceException {

        throw new UnresolvedReferenceException();
    }

}
