package com.example.toaextended.features.timetracking;

import com.example.toaextended.util.RaidRoom;

public enum SplitsMode
{

	OFF,
	ROOM,
	PATH,
	;

	boolean includesRoom(RaidRoom r)
	{
		if (this == OFF || r.getRoomType() == RaidRoom.RaidRoomType.LOBBY)
		{
			return false;
		}

		return this == ROOM ||
			r.getRoomType() == RaidRoom.RaidRoomType.BOSS;
	}

	String nextSplit(RaidRoom r)
	{
		if (this == OFF || r == null || r.getRoomType() == RaidRoom.RaidRoomType.LOBBY)
		{
			return null;
		}

		if (this == ROOM)
		{
			return r.toString();
		}

		switch (r)
		{
			case CRONDIS:
			case ZEBAK:
				return RaidRoom.ZEBAK.toString();

			case SCABARAS:
			case KEPHRI:
				return RaidRoom.KEPHRI.toString();

			case APMEKEN:
			case BABA:
				return RaidRoom.BABA.toString();

			case HET:
			case AKKHA:
				return RaidRoom.AKKHA.toString();

			case WARDEN_P2:
				return RaidRoom.WARDEN_P2.toString();
		}

		return null;
	}

}
