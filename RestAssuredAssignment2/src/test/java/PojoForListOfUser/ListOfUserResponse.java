package PojoForListOfUser;


import java.util.List;

public class ListOfUserResponse{
	private List<ListOfUserResponseItem> response;

	public void setResponse(List<ListOfUserResponseItem> response){
		this.response = response;
	}

	public List<ListOfUserResponseItem> getResponse(){
		return response;
	}
}