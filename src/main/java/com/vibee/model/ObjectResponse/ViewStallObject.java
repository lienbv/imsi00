package com.vibee.model.ObjectResponse;

import com.vibee.model.response.category.CategoryItem;
import com.vibee.model.response.export.ExportStallItem;

import java.util.List;

public interface ViewStallObject {
     String getNameSupplier();
     int getId();
     Double getInventory();
     int getUnitId();
     String getImg();
}
