package com.vibee.model.request.expired;

import com.vibee.model.item.EditPriceExportItem;
import com.vibee.model.request.BaseRequest;
import lombok.Data;

import java.util.List;

@Data
public class EditPriceExportRequest extends BaseRequest {
    private List<EditPriceExportItem> list;
    private int idImport;
}
