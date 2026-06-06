package com.invoice.api.service;

import java.util.List;

import com.invoice.api.dto.ApiResponse;
import com.invoice.api.dto.in.DtoCouponIn;
import com.invoice.api.entity.Coupon;

public interface SvcCoupon {

	List<Coupon> getAll();
	ApiResponse create(DtoCouponIn dto);
	ApiResponse delete(Integer id);
}
