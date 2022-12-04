package com.vibee.service.vunit;

import com.vibee.entity.VUnit;
import com.vibee.model.request.v_unit.UnitDeleteParentRequest;
import com.vibee.model.request.v_unit.UnitRequest;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.unit.GetUnitChildReponse;
import com.vibee.model.response.unit.GetUnitsResponse;

public interface UnitService {
    GetUnitChildReponse getUnitChild(int paretnId, String language);
    public GetUnitsResponse getAll(String nameType, int page, int record);
    public VUnit save(UnitRequest request);
    public VUnit update(UnitRequest request);
    public BaseResponse delete(int id);
    public BaseResponse deleteUnitParent(UnitDeleteParentRequest request);
}
