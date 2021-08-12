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
	@JsonSubTypes.Type(value = Employee.class, name = "Employee"),
	@JsonSubTypes.Type(value = Supervisor.class, name = "Supervisor"),
	@JsonSubTypes.Type(value = Head.class, name = "Head"),
	@JsonSubTypes.Type(value = Benco.class, name = "Benco")
})
public interface Role {

	String getName();
}
