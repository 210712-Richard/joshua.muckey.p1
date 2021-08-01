package com.revature.models;

public enum Department implements DepartmentHead{

	ACCOUNTING(){
		private String departmentHeadUserName;
		
		public String getHead() {
			return departmentHeadUserName;
		}
		public void setHead(String departmentHeadUserName) {
			this.departmentHeadUserName = departmentHeadUserName;
		}
	}
	,
	ENGINEERING(){

		private String departmentHeadUserName;
		
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
