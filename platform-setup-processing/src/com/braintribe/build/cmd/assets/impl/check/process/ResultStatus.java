package com.braintribe.build.cmd.assets.impl.check.process;

import com.braintribe.model.generic.base.EnumBase;
import com.braintribe.model.generic.reflection.EnumType;
import com.braintribe.model.generic.reflection.EnumTypes;

public enum ResultStatus implements EnumBase {
	success,
	error,
	skipped,
	failure;

	public static final EnumType<ResultStatus> T = EnumTypes.T(ResultStatus.class);

	@Override
	public EnumType<ResultStatus> type() {
		return T;
	}
}
