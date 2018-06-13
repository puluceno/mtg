package com.mtg.business.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.pmw.tinylog.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.jsoniter.JsonIterator;
import com.jsoniter.spi.TypeLiteral;
import com.mtg.business.AbstractBusiness;
import com.mtg.exception.CardNotFoundException;
import com.mtg.infrastructure.crawler.Crawler;
import com.mtg.model.Card;
import com.mtg.model.Result;
import com.mtg.model.Search;

@Primary
@Component
public class FilterBusiness<T> extends AbstractBusiness {

	public FilterBusiness(Crawler crawler) {
		super(crawler);
	}

	@Override
	public Object findPrices(String search) {
		List<Search> searchList = JsonIterator.deserialize(search, new TypeLiteral<List<Search>>() {
		});
		Map<String, List<Card>> find = find(searchList);
		return groupByStorePrice(find, searchList);
	}

	private Map<Object, Object> groupByStorePrice(Map<String, List<Card>> found, List<Search> search) {
		Map<Object, Object> end = new HashMap<>();

		search.forEach(s -> {
			found.forEach((k, v) -> {
				v.forEach(r -> {

					if (v.size() >= search.size() && r.getName().equalsIgnoreCase(s.getName())) {
						Map<Object, Object> key = new HashMap<>();
						key.put(k, v.stream().collect(
								Collectors.summingDouble(c -> c.getPrice().doubleValue() * s.getQty())));
						end.put(key, v);
					}
				});
			});
		});
		return end;
	}

	private Map<String, List<Card>> find(List<Search> search) {
		Logger.info("Requested data for " + search.size() + " cards.");

		return search.parallelStream().map(s -> {
			try {
				return new Result(s.getName(),
						buildStream(s).filter(r -> r.getQty() >= s.getQty())
								.filter(r -> r.isFoil() == s.isFoil())
								.limit(s.getLimit() > 0 ? s.getLimit() : Integer.MAX_VALUE)
								.collect(Collectors.toList()));
			} catch (CardNotFoundException e) {
				Logger.info(e.getMessage());
				return new Result(e.getMessage(), null);
			}
		}).flatMap(r -> r.getCards().stream()).collect(Collectors.groupingBy(Card::getStore));
	}

}
