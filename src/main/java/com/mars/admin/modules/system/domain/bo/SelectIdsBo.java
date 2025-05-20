package com.mars.admin.modules.system.domain.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class SelectIdsBo {

    {
        ids = new ArrayList<>();
    }

    private List<? extends Serializable> ids;

    public SelectIdsBo() {
    }

    public SelectIdsBo(List<? extends Serializable> ids) {
        this.ids = ids;
    }


}
