package com.pm.ftf;	
public class Truck {
		private String applicant;
		private String location;
		
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public String getApplicant() {
			return applicant;
		}
		public void setApplicant(String applicant) {
			this.applicant = applicant;
		}
		
		//for debugging
		public String toString() {
			return "  A: " + applicant + " L: " + location ;
		}
		
	}
