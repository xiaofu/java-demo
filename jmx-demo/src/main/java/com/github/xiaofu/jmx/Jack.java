package com.github.xiaofu.jmx;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

public class Jack extends NotificationBroadcasterSupport implements JackMBean {
	private int seq = 0;

	public void hi() {
		Notification n = new Notification(// ����һ����Ϣ��
				"jack.hi",// �����Notification�������
				this, // ��˭������Notification
				++seq,// һϵ��֪ͨ�е����к�,������������ֵ
				System.currentTimeMillis(),// ����ʱ��
				"Jack");// ��������Ϣ�ı�
		// ����ȥ
		sendNotification(n);
	}
}
