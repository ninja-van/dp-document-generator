package models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaperangAwbRequest {

    private List<PaperangAwb> awbs = new ArrayList<>();

}
