package net.hj2eplatform.core.serviceimpls;

import java.io.File;
import java.io.Serializable;
import java.math.BigInteger;
import net.hj2eplatform.core.exception.EntityNotFoundException;
import net.hj2eplatform.core.exception.RemoveEntityException;
import net.hj2eplatform.core.exception.SaveEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import net.hj2eplatform.core.exception.MySQLDuplicateRecordException;
import net.hj2eplatform.core.iservices.IAbstractService;
import net.hj2eplatform.core.models.Sequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * huyhoang for java platform. (h2j-platform)
 *
 * @author HuyHoang
 * @Email: huyhoang85_tb@yahoo.com
 * @Tel: 0966298666
 */
@Repository
@Transactional
public abstract class AbstractService<T> implements IAbstractService<T>, Serializable {

    @PersistenceContext
    protected EntityManager em;

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NEVER)
    public Long generateRandomIdNumber() {

        return Double.valueOf(Math.random() * 2000000000 + 1).longValue();
    }

    public void refresh(Object object) {
        if (em.contains(object)) {
            em.refresh(object);
        }

    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public T loadEntity(Class c, Object ref) {
        if (ref == null) {
            return null;
        }
        T t = (T) em.find(c, ref);
        if (t == null) {
            throw new EntityNotFoundException("Entity not found!");
        }
        return t;
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Object loadObject(Class c, Object ref) {
        if (ref == null) {
            return null;
        }
        Object t = em.find(c, ref);
        return t;
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<T> getAllEntity(Class c) {
        try {
            Query query = em.createQuery("Select e From " + c.getSimpleName() + " e");
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<T>();
        }
    }

    public String getCode(String prefix, int leng, boolean isAgent, String sequenceName) {
        StringBuilder stb = new StringBuilder(prefix);
        if (isAgent) {
            stb.append(".AG");
        } else {
            stb.append(".RT");
        }
        String sequen = getSequence(sequenceName).toString();
        for (int i = sequen.length(); i < 6; i++) {
            sequen = "0" + sequen;
        }
        return stb.append(".").append(sequen).toString();
    }

    public String getCode(String prefix, int leng, String sequenceName) {
        StringBuilder stb = new StringBuilder(prefix);
        String sequen = getSequence(sequenceName).toString();
        for (int i = sequen.length(); i < 6; i++) {
            sequen = "0" + sequen;
        }
        return stb.append(".").append(sequen).toString();
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void persistEntity(T t) throws RuntimeException {
        try {
            em.persist(t);
            em.flush();

        } catch (PersistenceException er) {
            er.printStackTrace();
            if (er.getMessage().indexOf("MySQLIntegrityConstraintViolationException") != -1) {
                throw new MySQLDuplicateRecordException();
            }
            throw er;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SaveEntityException("Can't save entity!");

        }
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void persistObject(Object t) throws RuntimeException {
        try {
            em.persist(t);
            em.flush();

        } catch (PersistenceException er) {
            er.printStackTrace();
            if (er.getMessage().indexOf("MySQLIntegrityConstraintViolationException") != -1) {
                throw new MySQLDuplicateRecordException();
            }
            throw er;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SaveEntityException("Can't save entity!");

        }
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public T saveEntity(T t) throws RuntimeException {
        try {
            t = em.merge(t);
            em.flush();
            return (T) t;
        } catch (PersistenceException er) {
            if (er.getMessage().indexOf("MySQLIntegrityConstraintViolationException") != -1) {
                throw new MySQLDuplicateRecordException();
            }
            throw er;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SaveEntityException("Can't save entity!");

        }
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Object saveObject(Object t) throws RuntimeException {
        try {
            t = em.merge(t);
            em.flush();
            return (T) t;
        } catch (PersistenceException er) {
            if (er.getMessage().indexOf("MySQLIntegrityConstraintViolationException") != -1) {
                throw new MySQLDuplicateRecordException();
            }
            throw er;
        } catch (Exception e) {
            throw new SaveEntityException("Can't save entity!");

        }
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public T removeEntity(T t) {
        t = em.merge(t);
        em.flush();
        em.remove(t);
        return t;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Object removeObject(Object t) {
        try {
            t = em.merge(t);
            em.flush();
            em.remove(t);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoveEntityException("Can't remove entity!");
        }
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public T removeEntity(Class c, Object ref) {
        try {
            T t = (T) loadEntity(c, ref);
            em.flush();
            em.remove(t);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoveEntityException("Can't remove entity!");
        }
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public BigInteger getSequence(String sequenceCode) {
        Sequence sequence = this.getSequenceObj(sequenceCode);
        if (sequence == null) {
            sequence = this.createdSequence(sequenceCode);
        }
        BigInteger value = null;

        if (sequence != null) {
            value = sequence.getValue();
        } else {
            value = this.createdSequence(sequenceCode).getValue();
        }
        sequence.setValue(sequence.getValue().add(BigInteger.valueOf(1)));
        em.persist(sequence);
        return value;

    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public String getSequenceCode(String sequenceCode) {
        return sequenceCode = sequenceCode + "." + this.getSequence(sequenceCode);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    protected Sequence getSequenceObj(String sequenceCode) {
        try {
            String sql = "select sequence.* from sequence sequence where sequence.sequence_Name = ?1 and sequence.status = 1 for update";
            Query query = em.createNativeQuery(sql, Sequence.class).setParameter(1, sequenceCode);
            return (Sequence) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }

    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    protected Sequence createdSequence(String sequenceCode) {
        Sequence sequence = new Sequence();
        sequence.setSequenceCode(sequenceCode);
        sequence.setSequenceName(sequenceCode);
        sequence.setValue(new BigInteger("1"));
        sequence.setStatus(1);
        return sequence;
    }

    @Autowired
    private MailSender mailSender;
    @Autowired
    private SimpleMailMessage templateMessage;

    public void testSendEmail() {

        templateMessage.setSubject("Day la tieu de");
        SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
        msg.setTo("hoangnh.ancoti@gmail.com");
        msg.setText("Dear Nguyễn Huy Hoàng, thank you for placing order. Your order number is ");
        try {
            this.mailSender.send(msg);
        } catch (MailException ex) {
            ex.printStackTrace();
        }
    }

    public void sendEmail(final String toEmail, final String title, final String content) {
        new Runnable() {

            @Override
            public void run() {
                templateMessage.setSubject(title);
                SimpleMailMessage msg = new SimpleMailMessage(templateMessage);
                msg.setTo(toEmail);
                msg.setText(content);
                try {
                    mailSender.send(msg);
                } catch (MailException ex) {
                    ex.printStackTrace();
                }
            }
        };
    }

    public void sendEmailAsHTML(final String toEmail, final String subject, final String content) {
        new Thread() {

            @Override
            public void run() {
                try {
                    JavaMailSenderImpl sender = new JavaMailSenderImpl();
                    sender.setHost("smtp.gmail.com");
                    sender.setPort(587);
                    sender.setUsername("bantour.vn@gmail.com");
                    sender.setPassword("nlntmttaum");
                    sender.setDefaultEncoding("UTF-8");
                    MimeMessage message = sender.createMimeMessage();

// use the true flag to indicate you need a multipart message
                    MimeMessageHelper helper = new MimeMessageHelper(message, true);
                    helper.setTo(toEmail);
                    helper.setSubject(subject);

// use the true flag to indicate the text included is HTML
                    helper.setText("<html><body>" + content + "</body></html>", true);

//// let's include the infamous windows Sample file (this time copied to c:/)
//                    FileSystemResource res = new FileSystemResource(new File("C:\\Users\\hoang_000\\Desktop\\logo.png"));
//                    helper.addInline("identifier1234", res);
                    sender.send(message);
                } catch (Exception ex) {

                }
            }
        }.start();
    }

    public static void main(String[] agrs) throws Exception {
        new Thread() {

            @Override
            public void run() {
                try {
                    JavaMailSenderImpl sender = new JavaMailSenderImpl();
                    sender.setHost("smtp.gmail.com");
                    sender.setPort(587);
                    sender.setUsername("bantour.vn@gmail.com");
                    sender.setPassword("nlntmttaum");

                    MimeMessage message = sender.createMimeMessage();

// use the true flag to indicate you need a multipart message
                    MimeMessageHelper helper = new MimeMessageHelper(message, true);
                    helper.setTo("vntravelsoft@gmail.com");
                    helper.setSubject("Duyệt nội dung tour du lịch: fadfadfda");

// use the true flag to indicate the text included is HTML
                    helper.setText("<html><body><img src='cid:identifier1234'> <br/> Vui long duyệt nội dung tour</body></html>", true);

// let's include the infamous windows Sample file (this time copied to c:/)
                    FileSystemResource res = new FileSystemResource(new File("C:\\Users\\hoang_000\\Desktop\\logo.png"));
                    helper.addInline("identifier1234", res);

                    sender.send(message);
                } catch (Exception ex) {

                }
            }
        }.start();
    }

    public MailSender getMailSender() {
        return mailSender;
    }

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public SimpleMailMessage getTemplateMessage() {
        return templateMessage;
    }

    public void setTemplateMessage(SimpleMailMessage templateMessage) {
        this.templateMessage = templateMessage;
    }

}
