package com.vibee.model.response.category;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TypeItemsDto {
    private TypeProductItems data;
    private List<TypeItemsDto> children;

    public TypeItemsDto() {
        this.data = new TypeProductItems();
        this.children = new ArrayList<TypeItemsDto>();
    }

    public List<TypeItemsDto> getChildren() {
        return children;
    }

    public void setChildren(List<TypeItemsDto> children) {
        this.children = children;
    }

    public void addChildren(TypeItemsDto children){
        if(!this.children.contains(children))
            this.children.add(children);
    }

}