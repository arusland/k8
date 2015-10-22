package io.arusland.k8.web;

import io.arusland.k8.dto.SearchSourceDto;
import io.arusland.k8.setting.SettingsManager;
import io.arusland.k8.source.SearchSource;
import io.arusland.k8.source.SourceOwner;
import io.arusland.k8.source.SourceType;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Optional;

/**
 * Created by ruslan on 20.10.2015.
 */
@Controller
public class SearchSourceController {
    @RequestMapping("/sources")
    public ModelAndView showAll() {
        ModelAndView view = new ModelAndView("sources");

        List<SearchSource> sources = SettingsManager.getInstance().loadSources();

        view.addObject("sources", sources);

        return view;
    }

    @RequestMapping("/sources/edit/{id}")
    public ModelAndView editSource(@PathVariable("id") Long id) {
        ModelAndView view = new ModelAndView("source-edit");
        Optional<SearchSource> source = SettingsManager
                .getInstance()
                .getSourceById(id);

        if (source.isPresent()) {
            view.addObject("source", SearchSourceDto.fromEntity(source.get()));
        }

        return view;
    }

    @RequestMapping(value = "/sources/edit", method = RequestMethod.POST)
    public String editSource(@ModelAttribute("source") SearchSourceDto source) {
        if (source != null) {
            SettingsManager.getInstance().update(source.toEntity());
        }

        return "redirect:/sources";
    }

    @RequestMapping("/sources/remove/{id}")
    public String removeSource(@PathVariable("id") Long id) {
        SettingsManager.getInstance().removeSourceById(id);

        return "redirect:/sources";
    }

    @RequestMapping("/sources/new")
    public ModelAndView addSource() {
        ModelAndView view = new ModelAndView("source-edit");

        // TODO: by default create private source
        view.addObject("source", SearchSourceDto
                .fromEntity(new SearchSource(SourceType.FileSystem,
                        StringUtils.EMPTY,
                        SourceOwner.DEFAULT)));

        return view;
    }

    @ModelAttribute("types")
    public SourceType[] getTypes() {
        return SourceType.values();
    }

    @ModelAttribute("owners")
    public SourceOwner[] getOwners() {
        return ArrayUtils.toArray(SourceOwner.DEFAULT);
    }
}
