package com.tripPortal.Observateur;

public interface Observer {

	void update(String event, Object data);

	void setSubject(Subject sub);
}