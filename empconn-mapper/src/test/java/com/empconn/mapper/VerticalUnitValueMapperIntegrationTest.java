package com.empconn.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.empconn.persistence.entities.Vertical;
import com.empconn.vo.UnitValue;

class VerticalUnitValueMapperIntegrationTest {

	private VerticalUnitValueMapper mapper = Mappers.getMapper(VerticalUnitValueMapper.class);

	@Test
	void givenSourceToDestination_whenMaps_thenCorrect() {
		Vertical source = new Vertical();
		source.setVerticalId(1);
		source.setName("sourceName");
		UnitValue destination = mapper.verticalToUnitValue(source);

		assertEquals(source.getName(), destination.getValue());
		assertEquals(source.getVerticalId(), Integer.parseInt(destination.getId()));
	}

	@Test
	void givenDestinationToSource_whenMaps_thenCorrect() {
		UnitValue destination = new UnitValue();
		destination.setValue("value 1");
		destination.setId("1000");
		Vertical source = mapper.unitValueToVertical(destination);
		assertEquals(destination.getValue(), source.getName());
		assertEquals(Integer.parseInt(destination.getId()), source.getVerticalId());
	}

}
