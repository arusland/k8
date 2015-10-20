package io.arusland.k8.web;

import io.arusland.k8.setting.SettingsManager;
import io.arusland.k8.source.SearchSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by ruslan on 20.10.2015.
 */
@Controller
public class SearchSourcesController {
    @RequestMapping("/sources")
    public ModelAndView showAll() {
        ModelAndView view = new ModelAndView("sources");

        List<SearchSource> sources = SettingsManager.getInstance().loadSources();

        view.addObject("sources", sources);

        return view;
    }
}
