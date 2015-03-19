# FoxRESTful

FoxRESTful is a RESful service based on Jersey RESTful Web Service. 

##### It has 2 entities and urls:
- Employees: /foxrestful/rest/employees
- Departments: /foxrestful/rest/departments

##### Supported methods:
- GET: get entity or group of entities
- POST: create new entity
- PUT: update avaliable entity
- DELETE: delete avaliable entity

For more details read **index.html**

### Tech:
- Jersey RESTful server (1.19)
- Hibernate (4.3.8.Final)
- MySQL
- Tomcat 7, 8 (or another container)
- Maven

### Installation

The following steps apply only for **Tomchat** container:

1. Clone repository to your machine.

2. Create user (for example: user="*deployer*", password="*s3cr3t*") for Tomcat in file *%TOMCAT_HOME%\conf\tomcat-users.xml* with roles *"manager-gui,manager-script"*

3. In *settings.xml* for your Maven (you can find this file in *%MAVEN_DIR%\conf\settings.xml* or *%PATH_TO_USER_DOCS%\\.m2\settings.xml*) create new server in section "servers" with your login/password pair:
    	<server>
			<id>MyTomcat</id>
			<username>deployer</username>
			<password>s3cr3t</password>
		</server>
4. If you change *id* section in previous step you must change server for Tomcat Maven Plugin in *pom.xml* of this project (configuration for tomcat maven plugin).

5. If your Tomcat servlet container has a non-standard 8080 port you must change it in *pom.xml* (configuration for tomcat maven plugin).

6. Create on MySQL Server DB: foxrest_db (or you can change this in hibernate.cfg.xml)

7. Create user="*lekarto*" and password="*1*" (or you can change this in hibernate.cfg.xml).

8. Give the full right for new user to DB from step 6.

9. Start Tomcat servlet container.

10. Open project directory in console.

11. Execute:

        mvn tomcat7:deploy

12. Open in your browser 

- [http://localhost:8080/foxrestful/rest/employees](http://localhost:8080/foxrestful/rest/employees) for get all employees

- [http://localhost:8080/foxrestful](http://localhost:8080/foxrestful) for reading FoxRESTful specification

That's all.

#### License
MIT
