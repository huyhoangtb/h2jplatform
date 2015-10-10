package net.hj2eplatform.iservices;

import net.hj2eplatform.core.iservices.ILazyDataSupportMapFilter;
import java.util.List;
import net.hj2eplatform.models.Organization;
import net.hj2eplatform.models.Human;
import net.hj2eplatform.utils.HumanType;
import net.hj2eplatform.utils.WorkingTitleDefine;

/**
 *
 * @author HoangNH
 */
public interface IHumanService extends ILazyDataSupportMapFilter<Human>{

    public List<Human> getHumanByWorkingTitle(Long rootOrgId, HumanType humanType, WorkingTitleDefine titleDefine);

    public List<Human> getHumanListByIdArr(String idArr);

    public List<Human> getHumanInOrg(Long orgId);

    public Boolean validateHumanByEmail(String email, Organization rootOrg, Long currentHumanId);

    public Boolean validateCustomerByEmail(String email, Organization rootOrg, Long currentCustomerId);

    public List<Human> suggestHuman(String input, Long rootOrgId, HumanType humanType, WorkingTitleDefine titleDefine, Integer maxRecored, Long notIncludeHumanId);

    public List<Human> getHumanByEmail(Long orgId, String emailAddress, Integer userType);

    public List<Human> suggestCustomer(String input, Organization rootSaleOrg, Integer maxRecored);

    public List<Human> suggestHuman(String input, Organization org, HumanType humanType, Integer maxRecored);

//    public List<Human> suggestCustomer(String input, Organization rootSaleOrg, Organization cusOrg, Integer maxRecored);
    public List<Human> getHumanByEmailAddressInRootOrg(Long rootOrg, String emailAddress, Integer userType);

    public List<Human> checkExistsHuman(Long rootOrg, String emailAddress, Integer userType, Long currentHumanId);

    /**
     * Hamf nay viet sau nen dung hon
     *
     * @param orgId
     * @param emailAddress
     * @param userType
     * @return
     */
    public Human getHumanByEmailAddress(Long orgId, String emailAddress, Integer userType);
//    public void saveEntity(HumanDto human, Organization organization, int toInteger);
}
