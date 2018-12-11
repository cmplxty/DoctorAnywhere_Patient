package anywhere.doctor.app.patient.doctor.dmcx.finalyearproject.Interface;

import java.util.List;

public interface ISearch {
    void onSearch(List<?> objects);
    List<?> getList();
    ISearch getiSearch();
}
