package com.littlehorizons.erp.dao;

import com.littlehorizons.erp.model.Teacher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class TeacherDaoImpl extends BaseJdbcDao implements TeacherDao {

    @Override
    public int insert(Teacher teacher) {
        String sql = "INSERT INTO profesores (tipo_documento, numero_documento, primer_nombre, segundo_nombre, " +
                "primer_apellido, segundo_apellido, correo_electronico, telefono, especializacion, fecha_contratacion, estado) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return insert(sql,
                teacher.documentType(),
                teacher.documentNumber(),
                teacher.firstName(),
                teacher.middleName(),
                teacher.lastName(),
                teacher.secondLastName(),
                teacher.email(),
                teacher.phone(),
                teacher.specialization(),
                teacher.hireDate(),
                teacher.status());
    }

    @Override
    public int update(Teacher teacher) {
        String sql = "UPDATE profesores SET tipo_documento = ?, numero_documento = ?, primer_nombre = ?, segundo_nombre = ?, " +
                "primer_apellido = ?, segundo_apellido = ?, correo_electronico = ?, telefono = ?, especializacion = ?, " +
                "fecha_contratacion = ?, estado = ? WHERE id_profesor = ?";
        return update(sql,
                teacher.documentType(),
                teacher.documentNumber(),
                teacher.firstName(),
                teacher.middleName(),
                teacher.lastName(),
                teacher.secondLastName(),
                teacher.email(),
                teacher.phone(),
                teacher.specialization(),
                teacher.hireDate(),
                teacher.status(),
                teacher.id());
    }

    @Override
    public Optional<Teacher> findById(int id) {
        String sql = "SELECT * FROM profesores WHERE id_profesor = ?";
        return queryOne(sql, this::map, id);
    }

    @Override
    public Optional<Teacher> findByDocument(String documentNumber) {
        String sql = "SELECT * FROM profesores WHERE numero_documento = ?";
        return queryOne(sql, this::map, documentNumber);
    }

    @Override
    public Optional<Teacher> findByEmail(String email) {
        String sql = "SELECT * FROM profesores WHERE correo_electronico = ?";
        return queryOne(sql, this::map, email);
    }

    @Override
    public List<Teacher> findAllActive() {
        String sql = "SELECT * FROM profesores WHERE estado = 'Activo' ORDER BY primer_apellido, segundo_apellido, primer_nombre";
        return query(sql, this::map);
    }

    @Override
    public List<Teacher> findAll() {
        String sql = "SELECT * FROM profesores ORDER BY primer_apellido, segundo_apellido, primer_nombre";
        return query(sql, this::map);
    }

    @Override
    public int updateStatus(int teacherId, String status) {
        String sql = "UPDATE profesores SET estado = ? WHERE id_profesor = ?";
        return update(sql, status, teacherId);
    }

    private Teacher map(ResultSet rs) throws SQLException {
        java.sql.Date date = rs.getDate("fecha_contratacion");
        LocalDate hireDate = date != null ? date.toLocalDate() : null;
        return new Teacher(
                rs.getInt("id_profesor"),
                rs.getString("tipo_documento"),
                rs.getString("numero_documento"),
                rs.getString("primer_nombre"),
                rs.getString("segundo_nombre"),
                rs.getString("primer_apellido"),
                rs.getString("segundo_apellido"),
                rs.getString("correo_electronico"),
                rs.getString("telefono"),
                rs.getString("especializacion"),
                hireDate,
                rs.getString("estado")
        );
    }
}
