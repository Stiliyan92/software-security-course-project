package Authentication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stili on 12/22/2016.
 */
public class CapabilityContainer {

    private List<Capability> capabilityList;

    public CapabilityContainer(){
        capabilityList = new ArrayList<Capability>();
    }

    public List<Capability> getCapabilityList() {
        return capabilityList;
    }

    public void setCapabilityList(List<Capability> capabilityList) {
        this.capabilityList = capabilityList;
    }

    public void addCapability(Capability c){
        capabilityList.add(c);
    }

    public void removeCapability(Capability c){
        capabilityList.remove(c);
    }
}
