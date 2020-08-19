# Pre-Requisites:
	Windows or Macbook machine with at least 16 GB RAM
	Install Eclipse IDE 2019‑12
	Install Eclipse plugins (Freemarker, Spring Boot, EditorConfig)
	Java  Runtime Environment 1.8
	Maven 3.5+
	Git Command Line
	SourceTree (optional)
	
	After Maven installation go to config folder and change settings.xml file to point to SDG artifactory.
	Make sure your have credentials created for SDG artifactory.

# Checkout the Source Code:
	git clone https://smartscm.dsg.gov.ae/scm/pps/paperless-platform.git
	Switch branch to {branchName} with below commands
	git checkout {branchName}
	git pull
# To create your branch
	git checkout feature/{branchtag}.  E.g. feature/dncommonplatform
# To commit the code
	git commit –m “commit message”
# To push the code
	git push origin feature/{branchtag}
    To merge the code, create pull request with destination as {destbranch}

# Build the jar/image:
	1. Run the below maven command to build the image
		mvn clean package
		
# Development Documentation:
1. All the common code which is generic for microservices will be available here.
2. Make sure to write test cases and sonar issues are resolved.
3. Place all the common configurations for MS in PlatformConfig.java file
4. All the specific configurations for MS should be implement IServiceConfig in each MS.
5. All the journey type's in each MS should be configured in a file which implement IJourneyType.
6. Rest API call's will be done using RestBasicAuthService and RestBasicAuthService for Authoriation with Bearer and Basic respectively.

# Deploy the jar to snapshot artifactory:
	This project is integrated with artifactory via Jenkins build automation once the pull request is merged.
	1. Raise the pull request with master branch.
	2. Pull request merge with master will trigger build.
	3. Once the build is successful SNAPSHOT jar is generated which will be pushed to snapshot artifactory.

# Deploy the jar to release artifactory:
	This project is integrated with artifactory via Jenkins build automation once the release branch is created.
	1. Create release branch from master branch.
	2. Trigger build manually.
	3. Once the build is successful jar is generated which will be pushed to release artifactory.


