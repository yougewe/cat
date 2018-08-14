package com.dianping.cat.status.model;

import com.dianping.cat.status.model.entity.DiskInfo;
import com.dianping.cat.status.model.entity.DiskVolumeInfo;
import com.dianping.cat.status.model.entity.Extension;
import com.dianping.cat.status.model.entity.ExtensionDetail;
import com.dianping.cat.status.model.entity.GcInfo;
import com.dianping.cat.status.model.entity.MemoryInfo;
import com.dianping.cat.status.model.entity.MessageInfo;
import com.dianping.cat.status.model.entity.OsInfo;
import com.dianping.cat.status.model.entity.RuntimeInfo;
import com.dianping.cat.status.model.entity.StatusInfo;
import com.dianping.cat.status.model.entity.ThreadsInfo;

public interface IVisitor {

   public void visitDisk(DiskInfo disk);

   public void visitDiskVolume(DiskVolumeInfo diskVolume);

   public void visitExtension(Extension extension);

   public void visitExtensionDetail(ExtensionDetail extensionDetail);

   public void visitGc(GcInfo gc);

   public void visitMemory(MemoryInfo memory);

   public void visitMessage(MessageInfo message);

   public void visitOs(OsInfo os);

   public void visitRuntime(RuntimeInfo runtime);

   public void visitStatus(StatusInfo status);

   public void visitThread(ThreadsInfo thread);
}
