package com.empconn.mapper.sample;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class SimpleSourceDestinationMapperIntegrationTest {

	private SimpleSourceDestinationMapper mapper = Mappers.getMapper(SimpleSourceDestinationMapper.class);

	@Test
	void givenSourceToDestination_whenMaps_thenCorrect() {
		SimpleSource simpleSource = new SimpleSource();
		simpleSource.setName("SourceName");
		simpleSource.setDescription("SourceDescription");
		SimpleDestination destination = mapper.sourceToDestination(simpleSource);

		assertEquals(simpleSource.getName(), destination.getName());
		assertEquals(simpleSource.getDescription(), destination.getDescription());
	}

	@Test
	void givenDestinationToSource_whenMaps_thenCorrect() {
		SimpleDestination destination = new SimpleDestination();
		destination.setName("DestinationName");
		destination.setDescription("DestinationDescription");
		SimpleSource source = mapper.destinationToSource(destination);
		assertEquals(destination.getName(), source.getName());
		assertEquals(destination.getDescription(), source.getDescription());
	}

}
