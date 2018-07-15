using System;
using System.Collections.Generic;
using System.Text;
using Qubit.BillSystem.Data;
using System.Linq;
using Microsoft.EntityFrameworkCore;

namespace %Namespace
{
    public class B%ClassName
    {
        /// <summary>
        /// Insert or update entity into database
        /// </summary>
        /// <param name="entityToSave"></param>
        /// <returns>id of inserted or updated entity</returns>
        public long Save(Entity.E%ClassName Entity)
        {
            try
            {
                using (var db = new Data.dbBillSystemContext())
                {
                    Data.%ClassName data;
                    if (Entity.Id == 0)
                    {
                        data = new %ClassName();
                        data.CreationDate = Entity.CreationDate;
                        data.CreationUser = Entity.CreationUser;
                        db.Add(data);
                    }
                    else
                    {
                        data = db.%ClassName.Where(x => x.Id == Entity.Id).FirstOrDefault();
                        data.ModificationDate = Entity.ModificationDate;
                        data.ModificationUser = Entity.ModificationUser;
                        db.SaveChanges();
                    }

                    %ReverseMap

                    db.SaveChanges();
                    return data.Id;
                }
            }
            catch (Exception ex)
            {
                BUtilLog.Write(BUtilLog.LogType.ERROR, ex);
                throw new Exception("Ocurrió un problema al intentar guardar, contacte al administrador");
            }

        }

        /// <summary>
        /// Returns all data that fit the state given
        /// </summary>
        /// <param name="State">state of the roles: 0 = none, 1 = enabled, 2 = disabled</param>
        /// <returns></returns>
        public List<Entity.E%ClassName> GetAll(int State)
        {
            try
            {
                using (var db = new Data.dbBillSystemContext())
                {
                    var dataList = db.%ClassName.ToList();

                    if (State > 0)
                    {
                        dataList = dataList.Where(x => x.State == State).ToList();
                    }
                    return MapList(dataList);
                }
            }
            catch (Exception ex)
            {
                BUtilLog.Write(BUtilLog.LogType.ERROR, ex);
                throw new Exception("Ocurrió  un problema al ejecutar la consulta, contacte al administrador");
            }
        }

        public Entity.E%ClassName GetById(long Id)
        {
            try
            {
                using (var db = new Data.dbBillSystemContext())
                {
                    var data = db.%ClassName
                           .Where(x => x.Id == Id)
                           .FirstOrDefault();
                    return Map(data);
                }
            }
            catch (Exception ex)
            {
                BUtilLog.Write(BUtilLog.LogType.ERROR, ex);
                throw new Exception("Ocurrió  un problema al ejecutar la consulta, contacte al administrador");
            }
        }

        public void ChangeState(long Id, int State)
        {
            try
            {
                using (var db = new Data.dbBillSystemContext())
                {
                    var data = db.%ClassName.Where(x => x.Id == Id).FirstOrDefault();
                    if (data == null)
                        throw new Exception("El registro no existe.");
                    data.State = (short)State;
                    db.SaveChanges();
                }
            }
            catch (Exception ex)
            {
                BUtilLog.Write(BUtilLog.LogType.ERROR, ex);
                throw new Exception("Ocurrió  un problema al ejecutar la consulta, contacte al administrador");
            }
        }

        private Entity.E%ClassName Map(Data.%ClassName Data)
        {
            if (Data == null) return null;
            Entity.E%ClassName entity = new Entity.E%ClassName();
            %Map

            return entity;
        }

        private Data.%ClassName ReverseMap(Entity.E%ClassName Entity)
        {
            if (Entity == null) return null;
            Data.%ClassName data = new Data.%ClassName();
            %ReverseMap

            return data;
        }

        public List<Entity.E%ClassName> MapList(List<Data.%ClassName> DataList)
        {
            if (DataList == null || DataList.Count == 0) return new List<Entity.E%ClassName>();
            var entityList = new List<Entity.E%ClassName>();
            foreach (var data in DataList)
            {
                var entity = Map(data);
                entityList.Add(entity);
            }
            return entityList;
        }

        public List<Data.%ClassName> ReverseMapList(List<Entity.E%ClassName> EntityList)
        {
            if (EntityList == null || EntityList.Count == 0) return new List<Data.%ClassName>();
            var dataList = new List<Data.%ClassName>();
            foreach (var entity in EntityList)
            {
                var data = ReverseMap(entity);
                dataList.Add(data);
            }
            return dataList;
        }
    }
}
