package com.invoice.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.invoice.api.dto.ApiResponse;
import com.invoice.api.dto.in.DtoCouponIn;
import com.invoice.api.entity.Coupon;
import com.invoice.api.repository.RepoCoupon;
import com.invoice.exception.ApiException;
import com.invoice.exception.DBAccessException;

@Service
public class SvcCouponImp implements SvcCoupon {

	@Autowired
	private RepoCoupon repo;

	@Override
	public List<Coupon> getAll() {
		try {
			return repo.findAll();
		} catch (DataAccessException e) {
			throw new DBAccessException();
		}
	}

	@Override
	public ApiResponse create(DtoCouponIn dto) {
		try {
			// Verificar que el codigo no exista
			if (repo.findByCodeAndActive(dto.getCode(), 1).isPresent()) {
				throw new ApiException(HttpStatus.CONFLICT,
						"Ya existe un cupon activo con el codigo: " + dto.getCode());
			}

			Coupon coupon = new Coupon();
			coupon.setCode(dto.getCode().toUpperCase());
			coupon.setDiscountPercentage(dto.getDiscountPercentage());
			coupon.setActive(1);
			repo.save(coupon);

			return new ApiResponse("El cupon ha sido registrado");
		} catch (DataAccessException e) {
			throw new DBAccessException();
		}
	}

	@Override
	public ApiResponse delete(Integer id) {
		try {
			Coupon coupon = repo.findById(id)
					.orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
							"El cupon no existe con id: " + id));

			if (coupon.getActive() == 0) {
				throw new ApiException(HttpStatus.BAD_REQUEST, "El cupon ya esta desactivado");
			}

			coupon.setActive(0);
			repo.save(coupon);

			return new ApiResponse("El cupon ha sido desactivado");
		} catch (DataAccessException e) {
			throw new DBAccessException();
		}
	}
}
