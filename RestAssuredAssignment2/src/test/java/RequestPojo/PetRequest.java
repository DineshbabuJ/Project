package RequestPojo;

import java.util.List;

public class PetRequest{
	private List<String> photoUrls;
	private String name;
	private long id;
	private Category category;
	private List<TagsItem> tags;
	private String status;

	public void setPhotoUrls(List<String> photoUrls){
		this.photoUrls = photoUrls;
	}

	public List<String> getPhotoUrls(){
		return photoUrls;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(long id){
		this.id = id;
	}

	public long getId(){
		return id;
	}

	public void setCategory(Category category){
		this.category = category;
	}

	public Category getCategory(){
		return category;
	}

	public void setTags(List<TagsItem> tags){
		this.tags = tags;
	}

	public List<TagsItem> getTags(){
		return tags;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}