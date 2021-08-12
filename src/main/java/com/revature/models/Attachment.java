package com.revature.models;

import java.util.Objects;
import java.util.UUID;

public class Attachment {
	
	private UUID id;
	private String filename;
	
	public Attachment() {
		id=UUID.randomUUID();
	}
	

	public Attachment(UUID id, String filename) {
		super();
		this.id = id;
		this.filename = filename;
	}


	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	@Override
	public int hashCode() {
		return Objects.hash(filename, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Attachment other = (Attachment) obj;
		return Objects.equals(filename, other.filename) && Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Attachment [id=" + id + ", filename=" + filename + "]";
	}
	

}
