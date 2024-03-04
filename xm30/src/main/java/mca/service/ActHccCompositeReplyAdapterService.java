package mca.service;

import com.inzent.igate.adapter.AdapterParameter;
import com.inzent.igate.core.exception.IGateException;
import com.inzent.igate.message.Record;
import com.inzent.igate.repository.meta.Activity;
import com.inzent.igate.repository.meta.Service;
import com.inzent.igate.service.activity.ActSyncReplyAdapterService;

public class ActHccCompositeReplyAdapterService extends ActSyncReplyAdapterService {

	public ActHccCompositeReplyAdapterService(Activity meta) {
		
	}
	
	protected AdapterParameter makeAdapterParameter(Service serviceMeta, Object request, Record paramRecord) throws IGateException {
		AdapterParameter  param = new AdapterParameter();
		
		return param;
	}
}
