<!--
Copyright 2015 The CHOReVOLUTION project

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
-->
# Syncope Identity Manager
This repository contains the Identity Manager (IdM). It is based on Apache Syncope project and it is responsible for managing users and services. In particular, the IdM is able to query the services for supported application contexts and played roles; force a specific application context for a certain service (put in "maintenance" or disable/enable).
Moreover, the IdM contains the Service Inventory that acts as a central repository for the description models of the services and things that can be used during the synthesis process.
    
## Requirements

* [Apache Maven 3.3.3+](https://maven.apache.org/install.html)
* [Java 8+](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
   
### Building

To build the project and generate the bundle use the following Maven command inside the folder [syncope-idm
/standalone](https://github.com/seagroup-univaq/syncope-idm/tree/master/standalone)

    mvn clean verify

If everything checks out, a zip named `idm-standalone-2.1.1-SNAPSHOT-distribution.zip` file containing the Syncope Identity Manager embedded in a tomcat web server should be available in the `/standalone/target` folder.
