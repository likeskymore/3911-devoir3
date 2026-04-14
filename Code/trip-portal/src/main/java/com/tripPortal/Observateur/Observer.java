package com.tripPortal.Observateur;

public interface Observer {

	void update(String event);

	void setSubject(Subject sub);
}