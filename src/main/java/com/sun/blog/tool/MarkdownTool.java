package com.sun.blog.tool;


import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.AttributeProviderContext;
import org.commonmark.renderer.html.AttributeProviderFactory;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * 创建者:Sun<br>
 * 创建时间:2023/1/20&nbsp;13:41<br>
 * 描述:com.sun.blog.tool<br>
 */
public class MarkdownTool{
	public static String MarkdownToHtml(String str){
		Parser parser = Parser.builder().build();
		Node node = parser.parse(str);
		HtmlRenderer renderer = HtmlRenderer.builder().build();
		return renderer.render(node);
	}
	public static String markdownAndIdAndTable(String str){
		//根据标题生成id
		Set<Extension> headID = Collections.singleton(HeadingAnchorExtension.builder().idPrefix("h").build());
		//转换表格
		Collection<Extension> table = Collections.singletonList(TablesExtension.create());
		Parser parser = Parser.builder().extensions(table).build();
		Node node = parser.parse(str);
		HtmlRenderer renderer = HtmlRenderer.builder().extensions(headID).extensions(table).attributeProviderFactory(attributeProviderContext -> new CustomAttributeProvider()).build();
		return renderer.render(node);
	}
	
	private static class CustomAttributeProvider implements AttributeProvider {
		@Override
		public void setAttributes(Node node, String s, Map<String, String> attributes) {
			//改变a标签的target属性为_blank
			if (node instanceof Link) {
				attributes.put("target", "_blank");
			}
		}
	}
}
