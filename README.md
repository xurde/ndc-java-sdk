# NDC Java SDK

This is a Java project which wraps NDC-compliant API.
It's host-agnostic so it can point to any NDC host.

## Installation

### Using Maven
Add the following dependency to your project's dependencies
```
    <dependency>
      <groupId>org.iata.ndc</groupId>
      <artifactId>ndc-client</artifactId>
      <version>0.1.4</version>
      <scope>compile</scope>
    </dependency>
```

### Using Gradle
Add the following dependency to your `dependencies` section
```
        compile 'org.iata.ndc:ndc-client:0.1.4'
```

### Manual installation
You can install NDC Java SDK from the [GitHub repository](https://github.com/iata-ndc/ndc-java-sdk) using git.
Run the following command:
```
git clone https://github.com/iata-ndc/ndc-java-sdk.git
cd ndc-java-sdk
mvn install -Dapi.key=<your api key>
```
Then add maven dependency as usual

## Usage

Add ndc-client to project dependencies

Request sample
```java
AirShoppingRQBuilder builder = new AirShoppingRQBuilder();
builder.addTravelAgencySender("Test agency", "0000XXXX", "test agent");
builder.addOriginDestination("CDG", "LHR", date);
builder.addAirlinePreference("C9");
AirShoppingRQ request = builder.build();

NdcClient client = new NdcClient("http://iata.api.mashery.com/kronos/api", "YOUR_API_KEY_HERE");
AirShoppingRS response = null;
try {
	response = client.airShopping(request);
} catch (IOException e) {
	<handle exception>
}
```
**NOTE:** At the moment only available builders are:
* AirShoppingRQBuilder
* SeatAvailabilityRQBuilder
* ServiceListRQBuilder 
* ServicePriceRQBuilder
All other requests can be constructed manually, using ObjectFactory in org.iata.ndc.schema package.
New common element builders will be added to org.iata.ndc.builder.element package.

This should return AirShoppingRS object, which contains response data.
