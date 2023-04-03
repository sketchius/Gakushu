package huhtala.bryce.jpgpt.model;

public class Set {

    private Integer id;
    private String name;
    private Integer itemTypeId;

    public Set() {
    }

    public Set(Integer id, String name, Integer itemTypeId, String expectedAnswer) {
        this.id = id;
        this.name = name;
        this.itemTypeId = itemTypeId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(Integer itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

}
