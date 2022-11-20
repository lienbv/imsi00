package com.vibee.service.vunit;

import com.vibee.model.response.unit.GetUnitChildReponse;

public interface UnitService {
    GetUnitChildReponse getUnitChild(int paretnId, String language);
}
