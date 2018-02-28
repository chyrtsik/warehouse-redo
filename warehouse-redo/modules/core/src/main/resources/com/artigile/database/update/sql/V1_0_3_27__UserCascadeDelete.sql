ALTER TABLE ReportState drop FOREIGN KEY reportstate_ibfk_1;
alter table ReportState add constraint FOREIGN KEY reportstate_ibfk_1 (user_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE FrameState drop FOREIGN KEY framestate_ibfk_1;
ALTER TABLE FrameState add constraint FOREIGN KEY framestate_ibfk_1 (user_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE ColumnState drop FOREIGN KEY columnstate_ibfk_1;
ALTER TABLE ColumnState add constraint FOREIGN KEY columnstate_ibfk_1 (reportState_id) REFERENCES reportstate (id) ON DELETE CASCADE;
