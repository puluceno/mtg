package com.mtg.business.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.pmw.tinylog.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;
import com.jsoniter.spi.TypeLiteral;
import com.mtg.business.AbstractBusiness;
import com.mtg.exception.CardNotFoundException;
import com.mtg.infrastructure.crawler.Crawler;
import com.mtg.model.Card;
import com.mtg.model.Search;

@Primary
@Component
public class FilterBusiness<T> extends AbstractBusiness {

	public FilterBusiness(Crawler crawler) {
		super(crawler);
	}

	@Override
	public String findPrices(String cards) {
		return JsonStream.serialize(find(cards));
	}

	private List<Card> find(String cards) {
		List<Search> search = JsonIterator.deserialize(cards, new TypeLiteral<List<Search>>() {
		});

		Logger.info("Requested data for " + search.size() + " cards.");

		return search.parallelStream().map(s -> {
			try {
				return new Card(s.getName(),
						buildStream(s).filter(r -> r.getQty() >= s.getQty())
								.filter(r -> s.isFoil() ? r.isFoil() == s.isFoil() : true)
								.limit(s.getLimit() > 0 ? s.getLimit() : Integer.MAX_VALUE)
								.collect(Collectors.toList()));
			} catch (CardNotFoundException e) {
				Logger.info(e.getMessage());
				return new Card(e.getMessage(), null);
			}
		}).collect(Collectors.toList());
		// .flatMap(c -> c.getResult().stream())
		// .collect(Collectors.summingDouble(r -> r.getPrice().doubleValue() *
		// r.getQty()));
	}

}
