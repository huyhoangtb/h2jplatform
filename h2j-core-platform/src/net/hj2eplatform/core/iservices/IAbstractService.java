package net.hj2eplatform.core.iservices;

import java.math.BigInteger;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * huyhoang for java platform. (h2j-platform)
 * @author HuyHoang
 * @Email: huyhoang85_tb@yahoo.com
 * @Tel: 0966298666
 */
@Repository
@Transactional
public interface IAbstractService<T> extends java.io.Serializable {

    public Long generateRandomIdNumber();

    public void refresh(Object object);

    public Object saveObject(Object t) throws RuntimeException;

    public String getCode(String prefix, int leng, boolean isAgent, String sequenceName);

    public String getCode(String prefix, int leng, String sequenceName);

    public T loadEntity(Class c, Object ref);

    public Object loadObject(Class c, Object ref);

    public List<T> getAllEntity(Class c);

    public Object removeObject(Object t);

    public T saveEntity(T t);

    public void persistEntity(T t);

    public void persistObject(Object t) throws RuntimeException;

    public T removeEntity(T t);

    public T removeEntity(Class c, Object ref);

    public BigInteger getSequence(String Code);

    public String getSequenceCode(String Code);

    public void sendEmail(final String toEmail, final String subject, final String content);

    public void sendEmailAsHTML(final String toEmail, final String subject, final String content);

    public void testSendEmail();
}
