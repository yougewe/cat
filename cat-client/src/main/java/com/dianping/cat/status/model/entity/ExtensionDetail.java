package com.dianping.cat.status.model.entity;

import com.dianping.cat.status.model.BaseEntity;
import com.dianping.cat.status.model.IVisitor;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.dianping.cat.status.model.Constants.ATTR_ID;
import static com.dianping.cat.status.model.Constants.ENTITY_EXTENSIONDETAIL;

public class ExtensionDetail extends BaseEntity<ExtensionDetail> {
   private String m_id;

   private double m_value;

   private Map<String, String> m_dynamicAttributes = new LinkedHashMap<String, String>();

   public ExtensionDetail() {
   }

   public ExtensionDetail(String id) {
      m_id = id;
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitExtensionDetail(this);
   }

   @Override
   public boolean equals(Object obj) {
      if (obj instanceof ExtensionDetail) {
         ExtensionDetail _o = (ExtensionDetail) obj;

         if (!equals(m_id, _o.getId())) {
            return false;
         }

         return true;
      }

      return false;
   }

   public String getDynamicAttribute(String name) {
      return m_dynamicAttributes.get(name);
   }

   public Map<String, String> getDynamicAttributes() {
      return m_dynamicAttributes;
   }

   public String getId() {
      return m_id;
   }

   public double getValue() {
      return m_value;
   }

   @Override
   public int hashCode() {
      int hash = 0;

      hash = hash * 31 + (m_id == null ? 0 : m_id.hashCode());

      return hash;
   }

   @Override
   public void mergeAttributes(ExtensionDetail other) {
      assertAttributeEquals(other, ENTITY_EXTENSIONDETAIL, ATTR_ID, m_id, other.getId());

      for (Map.Entry<String, String> e : other.getDynamicAttributes().entrySet()) {
         m_dynamicAttributes.put(e.getKey(), e.getValue());
      }

      m_value = other.getValue();
   }

   public void setDynamicAttribute(String name, String value) {
      m_dynamicAttributes.put(name, value);
   }

   public ExtensionDetail setId(String id) {
      m_id = id;
      return this;
   }

   public ExtensionDetail setValue(double value) {
      m_value = value;
      return this;
   }

}
