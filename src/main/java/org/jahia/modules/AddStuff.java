package org.jahia.modules.addStuff;

import java.util.Iterator;
import java.util.List;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.EndTag;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;
import org.apache.commons.lang.StringUtils;
import org.jahia.services.content.JCRPropertyWrapper;
import org.jahia.services.content.decorator.JCRSiteNode;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.filter.AbstractFilter;
import org.jahia.services.render.filter.RenderChain;
import org.jahia.services.render.filter.cache.AggregateCacheFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

public class AddStuff extends AbstractFilter
  implements ApplicationListener<ApplicationEvent>
{
  private static Logger logger = LoggerFactory.getLogger(AddStuff.class);

  public String execute(String previousOut, RenderContext renderContext, Resource resource, RenderChain chain)
    throws Exception
  {
    String out = previousOut;
    String addStuffHead = (resource.getNode().hasProperty("addStuffHead")) ? resource.getNode().getProperty("addStuffHead").getString() : null;
    String addStuffBody = (resource.getNode().hasProperty("addStuffBody")) ? resource.getNode().getProperty("addStuffBody").getString() : null;
    if ((StringUtils.isNotEmpty(addStuffHead)) || (StringUtils.isNotEmpty(addStuffBody))) {
      Iterator i$;
      Element element;
      Source source = new Source(previousOut);
      OutputDocument outputDocument = new OutputDocument(source);
      if (StringUtils.isNotBlank(addStuffHead)) {
        List headElementList = source.getAllElements("head");
        i$ = headElementList.iterator(); if (i$.hasNext()) { element = (Element)i$.next();
          EndTag headEndTag = element.getEndTag();

          outputDocument.replace(headEndTag.getBegin(), headEndTag.getBegin() + 1, "\n" + AggregateCacheFilter.removeEsiTags(addStuffHead) + "\n<");
        }

      }

      if (StringUtils.isNotBlank(addStuffBody)) {
        List bodyElementList = source.getAllElements("body");
        i$ = bodyElementList.iterator(); if (i$.hasNext()) { element = (Element)i$.next();
          EndTag bodyEndTag = element.getEndTag();

          outputDocument.replace(bodyEndTag.getBegin(), bodyEndTag.getBegin() + 1, "\n" + AggregateCacheFilter.removeEsiTags(addStuffBody) + "\n<");
        }

      }

      out = outputDocument.toString().trim();
    }

    return out;
  }

  public void onApplicationEvent(ApplicationEvent event)
  {
  }
}