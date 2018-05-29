package com.mtg.business.impl;

import org.springframework.stereotype.Component;

import com.mtg.business.AbstractBusiness;
import com.mtg.infrastructure.crawler.Crawler;

@Component
public class GroupBusiness extends AbstractBusiness {

	public GroupBusiness(Crawler crawler) {
		super(crawler);
	}

	@Override
	public String findPrices(String cards) {
		// TODO Auto-generated method stub
		return null;
	}

}
