# NDC Java SDK

This is a Java project which wraps NDC-compliant API.
It's host-agnostic so it can point to any NDC host.

## Installation

### Using Maven
Add the following dependency to your project's dependencies
```
    <dependency>
      <groupId>org.iata</groupId>
      <artifactId>ndc-client</artifactId>
      <version>0.1.0</version>
      <scope>compile</scope>
    </dependency>
```

### Using Gradle
Add the following dependency to your `dependencies` section
```
        compile 'org.iata:ndc-client:0.1.0'
```

### Manual installation
You can install NDC Java SDK from the [GitHub repository](https://github.com/iata-ndc/ndc-java-sdk) using git.
Run the following command:
```
git clone https://github.com/iata-ndc/ndc-java-sdk.git
cd ndc-java-sdk
mvn install
```
Then add maven dependency as usual

## Usage

Add ndc-client to project dependencies

Request sample
```
AirShoppingRQBuilder builder = new AirShoppingRQBuilder();
builder.addTravelAgencySender("Test agency", "0000XXXX", "test agent");
builder.addOriginDestination("CDG", "LHR", date);
AirShoppingRQ request = builder.build();

NdcClient client = new NdcClient("http://kronos.jrtechnologies.com/dondc");
AirShoppingRS response = null;
try {
	response = client.airShopping(request);
} catch (IOException e) {
	<handle exception>
}
```

This should return AirShoppingRS object, which contains response data.
