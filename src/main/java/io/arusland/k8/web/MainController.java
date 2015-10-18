package io.arusland.k8.web;

import io.arusland.k8.catalog.SearchObject;
import io.arusland.k8.search.SearchFilter;
import io.arusland.k8.search.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by ruslan on 27.06.2015.
 */
@Controller
public class MainController {
    public final SearchService searchService;

    @Autowired
    public MainController(SearchService searchService) {
        this.searchService = searchService;
    }

    @RequestMapping("/")
    public ModelAndView index(@RequestParam(value = "q", required = false) String text){
        ModelAndView view = new ModelAndView("index");

        if (StringUtils.isNotBlank(text)){
            text = text.trim();
            SearchFilter filter = SearchFilter.createPublicSearch(text);
            List<SearchObject> items = searchService.search(filter);

            view.addObject("items", items);
            view.addObject("searchText", text);
            view.addObject("itemsCount", items.size());
        }

        return view;
    }
}
