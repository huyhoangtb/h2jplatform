/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.iservices;

import net.hj2eplatform.core.iservices.ILazyDataSupportMapFilterAndObject;
import net.hj2eplatform.models.ContactUs;

/**
 *
 * @author Huy Hoang
 */
public interface IContactUsService extends ILazyDataSupportMapFilterAndObject<ContactUs> {

    public ContactUs getContactUs(Long contactId, Long randomNumber);
}
