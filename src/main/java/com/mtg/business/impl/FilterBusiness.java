package com.mtg.business.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.pmw.tinylog.Logger;

import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;
import com.jsoniter.spi.TypeLiteral;
import com.mtg.business.AbstractBusiness;
import com.mtg.business.Business;
import com.mtg.exception.CardNotFoundException;
import com.mtg.model.Card;
import com.mtg.model.ErrorMessage;
import com.mtg.model.Result;
import com.mtg.model.Search;
import com.mtg.model.enumtype.ErrorCode;

public class FilterBusiness<T> extends AbstractBusiness<Card> {

	private static class Helper {
		private static final Business<Result> INSTANCE = new FilterBusiness<Card>();
	}

	public static Business<Result> getInstance() {
		return Helper.INSTANCE;
	}

	@Override
	public String findPrices(String cards) {
		return JsonStream.serialize(find(cards));
	}

	private List<Object> find(String cards) {
		var search = JsonIterator.deserialize(cards, new TypeLiteral<List<Search>>() {
		});

		Logger.info("Requested data for " + search.size() + " cards.");

		return search.parallelStream().map(c -> {
			try {
				return new Card(c.getName(), buildStream(c.getName()).collect(Collectors.toList()));
			} catch (CardNotFoundException e) {
				Logger.info(e.getMessage());
				return new ErrorMessage(e.getMessage(), ErrorCode.CARD_NOT_FOUND);
			}
		}).collect(Collectors.toList());
	}

}
