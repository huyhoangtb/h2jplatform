package net.hj2eplatform.iservices;

import java.math.BigInteger;
import java.util.List;
import javax.faces.model.SelectItem;
import net.hj2eplatform.models.AppParam;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HoangNH
 */
@Repository
@Transactional
public interface IAbstractService<T> extends java.io.Serializable, net.hj2eplatform.core.iservices.IAbstractService {

    public String sqlSearchInOrg(Long rootOrgId, Long curentOrgId);

    public String sqlSearchInLoginOrg();

    public List<SelectItem> getParramItems(String groupCode, String code);

    public List<AppParam> getAppParamList(String groupCode, String code);

}
