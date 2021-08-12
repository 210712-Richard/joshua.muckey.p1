package com.revature.models;

public enum Department implements DepartmentHead{

	ACCOUNTING(){
		private String departmentHeadUserName = "josh";
		
		public String getHead() {
			return departmentHeadUserName;
		}
		public void setHead(String departmentHeadUserName) {
			this.departmentHeadUserName = departmentHeadUserName;
		}
	}
	,
	ENGINEERING(){

		private String departmentHeadUserName = "bob";
		
		@Override
		public String getHead() {
			return departmentHeadUserName;
		}

		@Override
		public void setHead(String departmentHeadUserName) {
			this.departmentHeadUserName = departmentHeadUserName;
		}
		
	}
	,
	BENCO(){
		private String departmentHeadUserName = "ben";
		
		@Override
		public String getHead() {
			return departmentHeadUserName;
		}

		@Override
		public void setHead(String departmentHeadUserName) {
			this.departmentHeadUserName = departmentHeadUserName;
		}
	}
}
