package com.dianping.cat.configuration.client.transform;

import com.dianping.cat.configuration.client.IEntity;
import com.dianping.cat.configuration.client.IVisitor;
import com.dianping.cat.configuration.client.entity.Bind;
import com.dianping.cat.configuration.client.entity.ClientConfig;
import com.dianping.cat.configuration.client.entity.Domain;
import com.dianping.cat.configuration.client.entity.Property;
import com.dianping.cat.configuration.client.entity.Server;

import static com.dianping.cat.configuration.client.Constants.ATTR_DUMP_LOCKED;
import static com.dianping.cat.configuration.client.Constants.ATTR_ENABLED;
import static com.dianping.cat.configuration.client.Constants.ATTR_HTTP_PORT;
import static com.dianping.cat.configuration.client.Constants.ATTR_ID;
import static com.dianping.cat.configuration.client.Constants.ATTR_IP;
import static com.dianping.cat.configuration.client.Constants.ATTR_MAX_MESSAGE_SIZE;
import static com.dianping.cat.configuration.client.Constants.ATTR_MODE;
import static com.dianping.cat.configuration.client.Constants.ATTR_NAME;
import static com.dianping.cat.configuration.client.Constants.ATTR_PORT;
import static com.dianping.cat.configuration.client.Constants.ELEMENT_BASE_LOG_DIR;
import static com.dianping.cat.configuration.client.Constants.ENTITY_BIND;
import static com.dianping.cat.configuration.client.Constants.ENTITY_CONFIG;
import static com.dianping.cat.configuration.client.Constants.ENTITY_DOMAIN;
import static com.dianping.cat.configuration.client.Constants.ENTITY_PROPERTIES;
import static com.dianping.cat.configuration.client.Constants.ENTITY_PROPERTY;
import static com.dianping.cat.configuration.client.Constants.ENTITY_SERVER;
import static com.dianping.cat.configuration.client.Constants.ENTITY_SERVERS;

public class DefaultXmlBuilder implements IVisitor {

   private IVisitor m_visitor = this;

   private int m_level;

   private StringBuilder m_sb;

   private boolean m_compact;

   public DefaultXmlBuilder() {
      this(false);
   }

   public DefaultXmlBuilder(boolean compact) {
      this(compact, new StringBuilder(4096));
   }

   public DefaultXmlBuilder(boolean compact, StringBuilder sb) {
      m_compact = compact;
      m_sb = sb;
      m_sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
   }

   public String buildXml(IEntity<?> entity) {
      entity.accept(m_visitor);
      return m_sb.toString();
   }

   protected void endTag(String name) {
      m_level--;

      indent();
      m_sb.append("</").append(name).append(">\r\n");
   }

   protected String escape(Object value) {
      return escape(value, false);
   }
   
   protected String escape(Object value, boolean text) {
      if (value == null) {
         return null;
      }

      String str = value.toString();
      int len = str.length();
      StringBuilder sb = new StringBuilder(len + 16);

      for (int i = 0; i < len; i++) {
         final char ch = str.charAt(i);

         switch (ch) {
         case '<':
            sb.append("&lt;");
            break;
         case '>':
            sb.append("&gt;");
            break;
         case '&':
            sb.append("&amp;");
            break;
         case '"':
            if (!text) {
               sb.append("&quot;");
               break;
            }
         default:
            sb.append(ch);
            break;
         }
      }

      return sb.toString();
   }
   
   protected void indent() {
      if (!m_compact) {
         for (int i = m_level - 1; i >= 0; i--) {
            m_sb.append("   ");
         }
      }
   }

   protected void startTag(String name) {
      startTag(name, false, null);
   }
   
   protected void startTag(String name, boolean closed, java.util.Map<String, String> dynamicAttributes, Object... nameValues) {
      startTag(name, null, closed, dynamicAttributes, nameValues);
   }

   protected void startTag(String name, java.util.Map<String, String> dynamicAttributes, Object... nameValues) {
      startTag(name, null, false, dynamicAttributes, nameValues);
   }

   protected void startTag(String name, Object text, boolean closed, java.util.Map<String, String> dynamicAttributes, Object... nameValues) {
      indent();

      m_sb.append('<').append(name);

      int len = nameValues.length;

      for (int i = 0; i + 1 < len; i += 2) {
         Object attrName = nameValues[i];
         Object attrValue = nameValues[i + 1];

         if (attrValue != null) {
            m_sb.append(' ').append(attrName).append("=\"").append(escape(attrValue)).append('"');
         }
      }

      if (dynamicAttributes != null) {
         for (java.util.Map.Entry<String, String> e : dynamicAttributes.entrySet()) {
            m_sb.append(' ').append(e.getKey()).append("=\"").append(escape(e.getValue())).append('"');
         }
      }

      if (text != null && closed) {
         m_sb.append('>');
         m_sb.append(escape(text, true));
         m_sb.append("</").append(name).append(">\r\n");
      } else {
         if (closed) {
            m_sb.append('/');
         } else {
            m_level++;
         }
   
         m_sb.append(">\r\n");
      }
   }

   protected void tagWithText(String name, String text, Object... nameValues) {
      if (text == null) {
         return;
      }
      
      indent();

      m_sb.append('<').append(name);

      int len = nameValues.length;

      for (int i = 0; i + 1 < len; i += 2) {
         Object attrName = nameValues[i];
         Object attrValue = nameValues[i + 1];

         if (attrValue != null) {
            m_sb.append(' ').append(attrName).append("=\"").append(escape(attrValue)).append('"');
         }
      }

      m_sb.append(">");
      m_sb.append(escape(text, true));
      m_sb.append("</").append(name).append(">\r\n");
   }

   protected void element(String name, String text, boolean escape) {
      if (text == null) {
         return;
      }
      
      indent();
      
      m_sb.append('<').append(name).append(">");
      
      if (escape) {
         m_sb.append(escape(text, true));
      } else {
         m_sb.append("<![CDATA[").append(text).append("]]>");
      }
      
      m_sb.append("</").append(name).append(">\r\n");
   }

   @Override
   public void visitBind(Bind bind) {
      startTag(ENTITY_BIND, true, null, ATTR_IP, bind.getIp(), ATTR_PORT, bind.getPort());
   }

   @Override
   public void visitConfig(ClientConfig config) {
      startTag(ENTITY_CONFIG, config.getDynamicAttributes(), ATTR_MODE, config.getMode(), ATTR_ENABLED, config.getEnabled(), ATTR_DUMP_LOCKED, config.getDumpLocked());

      element(ELEMENT_BASE_LOG_DIR, config.getBaseLogDir(), true);

      if (!config.getServers().isEmpty()) {
         startTag(ENTITY_SERVERS);

         for (Server server : config.getServers().toArray(new Server[0])) {
            server.accept(m_visitor);
         }

         endTag(ENTITY_SERVERS);
      }

      if (!config.getDomains().isEmpty()) {
         for (Domain domain : config.getDomains().values().toArray(new Domain[0])) {
            domain.accept(m_visitor);
         }
      }

      if (config.getBind() != null) {
         config.getBind().accept(m_visitor);
      }

      if (!config.getProperties().isEmpty()) {
         startTag(ENTITY_PROPERTIES);

         for (Property property : config.getProperties().values().toArray(new Property[0])) {
            property.accept(m_visitor);
         }

         endTag(ENTITY_PROPERTIES);
      }

      endTag(ENTITY_CONFIG);
   }

   @Override
   public void visitDomain(Domain domain) {
      startTag(ENTITY_DOMAIN, true, null, ATTR_ID, domain.getId(), ATTR_IP, domain.getIp(), ATTR_ENABLED, domain.getEnabled(), ATTR_MAX_MESSAGE_SIZE, domain.getMaxMessageSize());
   }

   @Override
   public void visitProperty(Property property) {
      startTag(ENTITY_PROPERTY, property.getText(), true, null, ATTR_NAME, property.getName());
   }

   @Override
   public void visitServer(Server server) {
      startTag(ENTITY_SERVER, true, null, ATTR_IP, server.getIp(), ATTR_PORT, server.getPort(), ATTR_HTTP_PORT, server.getHttpPort(), ATTR_ENABLED, server.getEnabled());
   }
}
