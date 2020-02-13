/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 or later of the                                  *
 * GNU General Public License as published                                    *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * Copyright (C) 2003-2019 E.R.P. Consultores y Asociados, C.A.               *
 * All Rights Reserved.                                                       *
 * Contributor(s): Yamel Senih www.erpya.com                                  *
 *****************************************************************************/
package org.spin.util.print;

import java.io.File;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.util.Env;
import org.compiere.util.Util;
import org.spin.model.MADAppRegistration;
import org.spin.util.support.AppSupportHandler;
import org.spin.util.support.IAppSupport;
import org.spin.util.support.mq.AbstractMessageQueue;
import org.spin.util.support.mq.PrinterMessage;

public class TestLocalPrinting {
	public static void main(String args[]) throws Exception {
		//	Validate
    	if(args == null) {
    		throw new Exception("Arguments Not Found");
    	}
    	//	
    	if(args.length < 2) {
    		throw new Exception("Arguments Must Be: [File Name, App Registration Id]");
    	}
    	// Is there a printer name?
    	String fileNameName = args[0];
    	//	Get sleep interval
    	int appRegisteredId = Integer.parseInt(args[1]);
    	org.compiere.Adempiere.startup(true);
		Env.setContext(Env.getCtx(), "#AD_Client_ID", 11);
		if(appRegisteredId <= 0) {
			throw new AdempiereException("@AD_AppRegistration_ID@ @NotFound@");
		}
		MADAppRegistration registeredApplication = MADAppRegistration.getById(Env.getCtx(), appRegisteredId, null);
		System.out.println("#################################################################");
		System.out.println("App Registered: " + registeredApplication);
		IAppSupport supportedApplication = AppSupportHandler.getInstance().getAppSupport(registeredApplication);
		System.out.println("Supported Class: " + supportedApplication.getClass().getName());
		//	Exists a Application available for it?
		if(supportedApplication != null
				&& AbstractMessageQueue.class.isAssignableFrom(supportedApplication.getClass())) {
			AbstractMessageQueue messageQueue = (AbstractMessageQueue) supportedApplication;
			//	Send message
			File file = new File(fileNameName);
			PrinterMessage messageToPrint = new PrinterMessage(file);
			System.out.println("File to send: " + file);
			String channel = registeredApplication.getParameterValue("TargetChannel");
			System.out.println("Channel: " + channel);
			if(Util.isEmpty(channel)) {
				throw new AdempiereException("@PrintChannel@ @NotFound@");
			}
			messageQueue.connect();
			messageQueue.publish(channel, messageToPrint);
			messageQueue.disconnect();
			System.out.println("File Sent");
		}
		System.out.println("#################################################################");
	}
}
