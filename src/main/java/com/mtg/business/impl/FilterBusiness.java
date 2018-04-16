package com.mtg.business.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.pmw.tinylog.Logger;

import com.jsoniter.output.JsonStream;
import com.mtg.business.AbstractBusiness;
import com.mtg.business.Business;
import com.mtg.exception.CardNotFoundException;
import com.mtg.model.Card;
import com.mtg.model.ErrorMessage;
import com.mtg.model.Result;
import com.mtg.model.enumtype.ErrorCode;

public class FilterBusiness<T> extends AbstractBusiness<Card> {

	private static class Helper {
		private static final Business<Card> INSTANCE = new FilterBusiness();
	}

	public static Business<Card> getInstance() {
		return Helper.INSTANCE;
	}

	@Override
	public String findPrices(String[] cards) {
		return JsonStream.serialize(find(cards));
	}

	private List<Object> find(String[] cards) {
		return Arrays.asList(cards).parallelStream().map(c -> {
			try {
				Stream<Result> buildStream = buildStream(c);
				List<Result> collect = buildStream.collect(Collectors.toList());
				return new Card(c, collect);
			} catch (CardNotFoundException e) {
				Logger.info(e.getMessage());
				return new ErrorMessage(e.getMessage(), ErrorCode.CARD_NOT_FOUND);
			}
		}).collect(Collectors.toList());
	}

}
