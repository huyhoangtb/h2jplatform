package net.hj2eplatform.iservices;

import net.hj2eplatform.core.iservices.ILazyDataSupportMapFilter;
import net.hj2eplatform.core.component.LazyDataSupportMapFilter;
import net.hj2eplatform.dto.HumanDto;
import net.hj2eplatform.models.Organization;
import net.hj2eplatform.models.Users;
import net.hj2eplatform.utils.HumanType;

/**
 *
 * @author HoangNH
 */
public interface IHumanDtoService extends ILazyDataSupportMapFilter<HumanDto> {

    public final static String USER_SEARCH_DATA = "ORGANIZATION_SEARCH_DATA";
    public final static String USER_PARENT_PATH = "USER_PARENT_PATH";
    public final static String USER_ORGNAZATION_ID = "USER_ORGNAZATION_ID";
    public final static String STAFF_TYPE = "STAFF_TYPE";
    public final static String USER_ORGNAZATION_ROOT_ID = "USER_ORGNAZATION_ROOT_ID";
    public final static String USER_USERNAME = "USER_USERNAME";
    public final static String USER_EMAIL = "USER_EMAIL";
    public final static String USER_BITHDAY = "USER_BITHDAY";
    public final static String USER_FULLNAME = "USER_FULLNAME";
    public final static String USER_TEL = "USER_TEL";
    public final static String USER_GENDER = "USER_GENDER";
    public final static String PARRENT_ID = "PARRENT_ID";
    public final static String ORG_TYPE = "OBJECT_TYPE";

    public void saveHuman(HumanDto human, Organization organization, HumanType humanType);
    
    public void saveHuman2(HumanDto human, Organization organization, HumanType humanType);

    public void setParameter4Search(Organization organization, LazyDataSupportMapFilter<HumanDto> humanLazyDataModel, HumanDto searchData, Integer orgType);

    public HumanDto loadHumanDto(Long humanId);

    /**
     *
     * @param human
     * @param organization
     * @param humanType
     */
    public void saveHumanOnly(HumanDto human, Organization organization, HumanType humanType);

    public void saveUserOnly(HumanDto human);
    

    public void updateUserPassword(Users user);

    public void validateHuman(HumanDto human, Organization organization, HumanType humanType);

    public void validateUserAccountOnly(Users human);

    public void validateHumanOnly(HumanDto human, Organization organization, HumanType humanType);
}
