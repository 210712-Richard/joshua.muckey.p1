package com.revature.models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = "type",
		defaultImpl = Employee.class
		)
@JsonSubTypes({
	@JsonSubTypes.Type(value = Employee.class, name = "Employee")
})
public interface Role {

	String getName();
}
